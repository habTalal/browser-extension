package com.amdocs.innovations.browserextension.models;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class EnvironmentsDTO implements Serializable {

    String projectName;
    List<ClusterDTO> clusters;

}
