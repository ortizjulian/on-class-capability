package com.on_class.capability.infrastructure.utils;

public class Constants {
    private Constants() {
        throw new UnsupportedOperationException(UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED);
    }

    public static final String UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED = "Utility class should not be instantiated";

    //Routes
    public static final String CAPABILITY_ROUTE = "/capability";

    //Table
    public static final String CAPABILITY_TABLE_NAME = "capability";

    //Dto Validations
    public static final String EXCEPTION_CAPABILITY_NAME_NULL = "The technology name cannot be null";
    public static final String EXCEPTION_CAPABILITY_DESCRIPTION_NULL = "The technology description cannot be null";

    //EXCEPTIONS
    public static final String CAPABILITY_ERROR = "Error on Capability - [ERROR]";
    public static final String NO_ADDITIONAL_DETAILS = "No additional details available.";

    //Technology WebClient
    public static final String PROPERTIES_PREFIX_TECHNOLOGY = "technology";
    public static final String TECHNOLOGY_ROUTE = "/technology/link-capabilities/";
}
