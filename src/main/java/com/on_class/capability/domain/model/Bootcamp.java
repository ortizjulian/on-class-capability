package com.on_class.capability.domain.model;

import java.util.List;

public class Bootcamp {
    private Long id;
    private List<Capability> capabilities;

    public Bootcamp(Long id, List<Capability> capabilities) {
        this.id = id;
        this.capabilities = capabilities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Capability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = capabilities;
    }
}
