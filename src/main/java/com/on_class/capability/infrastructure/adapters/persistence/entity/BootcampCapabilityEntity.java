package com.on_class.capability.infrastructure.adapters.persistence.entity;

import com.on_class.capability.infrastructure.utils.Constants;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(Constants.BOOTCAMP_CAPABILITY_TABLE_NAME)
@Getter
@Setter
@Builder
public class BootcampCapabilityEntity {

    @Id
    private Long id;
    private Long bootcampId;
    private Long capabilityId;
}
