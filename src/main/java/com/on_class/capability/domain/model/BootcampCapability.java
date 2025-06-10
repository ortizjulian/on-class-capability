package com.on_class.capability.domain.model;

public class BootcampCapability {
    private Long bootcampId;
    private Long capabilityId;

    public BootcampCapability(Long bootcampId, Long capabilityId) {
        this.bootcampId = bootcampId;
        this.capabilityId = capabilityId;
    }

    public Long getBootcampId() {
        return bootcampId;
    }

    public void setBootcampId(Long bootcampId) {
        this.bootcampId = bootcampId;
    }

    public Long getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(Long capabilityId) {
        this.capabilityId = capabilityId;
    }
}
