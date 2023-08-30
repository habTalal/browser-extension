package com.amdocs.innovations.browserextension.controller;

import com.amdocs.innovations.browserextension.interfaces.controller.IBrowserExtensionController;
import com.amdocs.innovations.browserextension.interfaces.services.IRetrieveCdEnvironmentsService;
import com.amdocs.innovations.browserextension.interfaces.services.IRetrieveProjectClustersService;
import com.amdocs.innovations.browserextension.models.EnvironmentsDTO;
import com.amdocs.innovations.model.CdEnvironmentsDetailsResponse;
import com.amdocs.innovations.model.ClusterDetailsResponse;
import com.amdocs.innovations.model.ProjectClustersDetailsResponse;
import com.amdocs.innovations.model.ProjectDetailsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.ApiResponse;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1Secret;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Controller
public class BrowserExtensionController implements IBrowserExtensionController
{

    @Autowired
    private IRetrieveCdEnvironmentsService retrieveCdEnvironmentsService;

    @Autowired
    private IRetrieveProjectClustersService retrieveProjectClustersService;
    private ObjectMapper objectMapper = new ObjectMapper();
    public List<ClusterDetailsResponse> retrieveClusters(String projectName)
    {
        AtomicReference<List<ClusterDetailsResponse>> clusterDetailsResponse = new AtomicReference<>(new ArrayList<>());
        try {
            List<EnvironmentsDTO> environmentsDTO
                    = objectMapper.readValue(readFromFile("environmets.json"),
                    new TypeReference<>() {
                    });
            log.info("test {}",environmentsDTO);

            environmentsDTO.stream().filter(itm -> itm.getProjectName().equals(projectName))
                    .findFirst()    .ifPresent(itm -> itm.getClusters().forEach(itm1 -> {
                        clusterDetailsResponse.get().add(new ClusterDetailsResponse().clusterName(itm1.getName()));
                    }));



        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return clusterDetailsResponse.get();
    }

    public static String readFromFile(String fileName) {

        InputStream in = BrowserExtensionController.class.getClassLoader().getResourceAsStream(fileName);

        String content = "";

        try (Scanner scanner = new Scanner(in)) {
            content = scanner.useDelimiter("\\Z").next();
        }

        return content;
    }

    public List<ProjectDetailsResponse> retrieveProjects()
    {
        log.info("test retrieveProjects - test");
        return null;
    }

    @Override
    public CdEnvironmentsDetailsResponse retrieveCdEnvironments(String projectName, String clusterName) {
        return retrieveCdEnvironmentsService.retrieveCdEnvironmentsService(projectName,clusterName);
    }

    @Override
    public ProjectClustersDetailsResponse retrieveProjectClusters() {
        return retrieveProjectClustersService.retrieveProjectClustersService();
    }


}
