package com.on_class.capability.infrastructure.adapters.persistence.entity;

import com.on_class.capability.infrastructure.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(Constants.CAPABILITY_TABLE_NAME)
@Getter
@Setter
public class CapabilityEntity {

    @Id
    private Long id;
    private String name;
    private String description;
}
