package com.amdocs.innovations.browserextension.interfaces.services;

import com.amdocs.innovations.model.CdEnvironmentsDetailsResponse;

import java.util.List;

public interface IRetrieveCdEnvironmentsService {
    CdEnvironmentsDetailsResponse retrieveCdEnvironmentsService(String projectName, String clusterName);
}
