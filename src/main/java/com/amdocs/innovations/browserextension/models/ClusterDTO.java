package com.amdocs.innovations.browserextension.models;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ClusterDTO implements Serializable {
    String name;
    String token;
}
