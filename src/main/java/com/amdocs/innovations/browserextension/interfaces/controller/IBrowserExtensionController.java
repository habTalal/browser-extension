package com.amdocs.innovations.browserextension.interfaces.controller;

import com.amdocs.innovations.model.CdEnvironmentsDetailsResponse;
import com.amdocs.innovations.model.ClusterDetailsResponse;
import com.amdocs.innovations.model.ProjectClustersDetailsResponse;
import com.amdocs.innovations.model.ProjectDetailsResponse;

import java.util.List;

public interface IBrowserExtensionController {
    List<ClusterDetailsResponse> retrieveClusters(String projectName);
    List<ProjectDetailsResponse> retrieveProjects();

    CdEnvironmentsDetailsResponse retrieveCdEnvironments(String projectName, String clusterName);

    ProjectClustersDetailsResponse retrieveProjectClusters();
}
