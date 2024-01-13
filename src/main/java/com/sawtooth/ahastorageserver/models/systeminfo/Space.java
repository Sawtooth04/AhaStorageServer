package com.sawtooth.ahastorageserver.models.systeminfo;

import org.springframework.hateoas.RepresentationModel;

public class Space extends RepresentationModel<Space> {
    public double freeSpace;
    public double totalSpace;
}
