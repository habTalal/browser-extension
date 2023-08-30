package com.amdocs.innovations.browserextension.rest;

import com.amdocs.innovations.api.BrowserextensionApi;
import com.amdocs.innovations.browserextension.interfaces.controller.IBrowserExtensionController;
import com.amdocs.innovations.model.CdEnvironmentsDetailsResponse;
import com.amdocs.innovations.model.ClusterDetailsResponse;
import com.amdocs.innovations.model.ProjectClustersDetailsResponse;
import com.amdocs.innovations.model.ProjectDetailsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class BrowserExtensionResource implements BrowserextensionApi {

    @Autowired
    private IBrowserExtensionController browserExtensionController;

    @Override
    public ResponseEntity<List<ClusterDetailsResponse>> retrieveClusters(String projectName) {

        return ResponseEntity.status(HttpStatus.OK).body(browserExtensionController.retrieveClusters(projectName));
    }

    public ResponseEntity<List<ProjectDetailsResponse>> retrieveProjects() {
        log.info("test retrieveProjects");
        return ResponseEntity.status(HttpStatus.OK).body(browserExtensionController.retrieveProjects());
    }

    @Override
    public ResponseEntity<ProjectClustersDetailsResponse> retrieveProjectClusters() {
        return ResponseEntity.status(HttpStatus.OK).body(browserExtensionController.retrieveProjectClusters());
    }

    @Override
    public ResponseEntity<CdEnvironmentsDetailsResponse> retrieveCdEnvironments(String projectName,String clusterName) {
        return ResponseEntity.status(HttpStatus.OK).body(browserExtensionController.retrieveCdEnvironments(projectName,clusterName));
    }
}
