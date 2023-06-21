package com.bezkoder.spring.files.upload.utils;

public class FieldConfig {
    private String name;
    private boolean required;
    private Integer maxLength;
    private String dataType;
    private String dateFormat;

    public boolean isRequired() {
        return required;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public String getDataType() {
        return dataType;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getName() {
        return name;
    }

    // Getters and setters
}
