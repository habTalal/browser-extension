package com.amdocs.innovations.browserextension.services;

import com.amdocs.innovations.browserextension.controller.BrowserExtensionController;
import com.amdocs.innovations.browserextension.interfaces.services.IRetrieveProjectClustersService;
import com.amdocs.innovations.browserextension.models.ClusterDTO;
import com.amdocs.innovations.browserextension.models.EnvironmentsDTO;
import com.amdocs.innovations.model.ClusterDetailsResponse;
import com.amdocs.innovations.model.ProjectClustersDetailsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class RetrieveProjectClustersService implements IRetrieveProjectClustersService {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public ProjectClustersDetailsResponse retrieveProjectClustersService() {

        ProjectClustersDetailsResponse projectClustersDetailsResponse = new ProjectClustersDetailsResponse();
        Map<String,List<String>> projectClustersMap = new HashMap<>();

        AtomicReference<List<ClusterDetailsResponse>> clusterDetailsResponse = new AtomicReference<>(new ArrayList<>());
        try {
            List<EnvironmentsDTO> environmentsDTO
                    = objectMapper.readValue(readFromFile("environmets.json"),
                    new TypeReference<>() {
                    });
            log.info("test {}",environmentsDTO);

            environmentsDTO.stream().filter(itm -> itm.getProjectName().equals("projectName"))
                    .findFirst()    .ifPresent(itm -> itm.getClusters().forEach(itm1 -> {
                        clusterDetailsResponse.get().add(new ClusterDetailsResponse().clusterName(itm1.getName()));
                    }));

            for (EnvironmentsDTO envDto : environmentsDTO) {
                List<String> clustersList = envDto.getClusters().stream().map(ClusterDTO::getName).toList();
                projectClustersMap.put(envDto.getProjectName(),clustersList);

            }
            projectClustersDetailsResponse.setProjectClusters(projectClustersMap);



        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return projectClustersDetailsResponse;
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
