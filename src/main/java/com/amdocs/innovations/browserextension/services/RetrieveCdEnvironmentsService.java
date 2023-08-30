package com.amdocs.innovations.browserextension.services;

import com.amdocs.innovations.browserextension.controller.BrowserExtensionController;
import com.amdocs.innovations.browserextension.interfaces.services.IRetrieveCdEnvironmentsService;
import com.amdocs.innovations.browserextension.models.EnvironmentsDTO;
import com.amdocs.innovations.model.CdEnvironmentsDetailsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class RetrieveCdEnvironmentsService implements IRetrieveCdEnvironmentsService {

    private ObjectMapper objectMapper = new ObjectMapper();

    private final static String K8S_URL = "https://api.CLUSTER_NAME.ocpd.corp.amdocs.com:6443";

    @Override
    public CdEnvironmentsDetailsResponse retrieveCdEnvironmentsService(String projectName, String clusterName) {
        log.info("test retrieveCdEnvironmentsService");
        CdEnvironmentsDetailsResponse cdEnvironmentsDetailsResponse = new CdEnvironmentsDetailsResponse();
        List<EnvironmentsDTO> environmentsDTO
                = null;
        try {
            environmentsDTO = objectMapper.readValue(readFromFile("environmets.json"),
                    new TypeReference<>() {
                    });
            AtomicReference<String> token = new AtomicReference<>();
            environmentsDTO.stream()
                    .filter(env -> env.getProjectName().equals(projectName))
                    .flatMap(env -> env.getClusters().stream())
                    .filter(cluster -> cluster.getName().equals(clusterName))
                    .findFirst()
                    .ifPresent(cluster -> token.set(cluster.getToken()));

            List<String> cds = retrieveCdEnvironments(clusterName, token.get());

            cdEnvironmentsDetailsResponse.setClusterName(clusterName);
            cdEnvironmentsDetailsResponse.setProjectName(projectName);
            cdEnvironmentsDetailsResponse.setCdEnvironments(cds);
            

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("test {}", environmentsDTO);
        return cdEnvironmentsDetailsResponse;
    }

    public List<String> retrieveCdEnvironments(String clusterName, String token) {
        String k8sUrl = K8S_URL.replace("CLUSTER_NAME", clusterName);
        CoreV1Api kubernetesAPI = null;
        Configuration.setDefaultApiClient(Config.fromToken(k8sUrl, token, false));
        kubernetesAPI = new CoreV1Api();
        List<String> cds;
        try {

            V1NamespaceList namespaceList = kubernetesAPI.listNamespace(null, null, null, null, null, null, null, null, 10, false);
            log.info(objectMapper.writeValueAsString(namespaceList));
            cds = new ArrayList<>();
            for (V1Namespace item : namespaceList.getItems()) {
                String obj = item.getMetadata().getAnnotations().get("openshift.io/display-name");

                if (obj == null || obj.equals("null") || obj.isEmpty()) {
                    continue;
                }

                cds.add(obj);
            }
            log.info("cds {}", cds);

        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return cds;

    }


    public static String readFromFile(String fileName) {

        InputStream in = BrowserExtensionController.class.getClassLoader().getResourceAsStream(fileName);

        String content = "";

        try (Scanner scanner = new Scanner(in)) {
            content = scanner.useDelimiter("\\Z").next();
        }

        return content;
    }
}
