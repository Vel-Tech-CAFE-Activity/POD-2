package com.files.upload.model;

import com.files.upload.utils.FieldConfig;

import java.util.List;

public class Configuration {
    private int maxColumnCount;
    private int maxFileSize;
    private List<FieldConfig> fieldConfigs;


    public int getMaxColumnCount() {
        return maxColumnCount;
    }

    public void setMaxColumnCount(int maxColumnCount) {
        this.maxColumnCount = maxColumnCount;
    }

    public int getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public List<FieldConfig> getFieldConfigs() {
        return fieldConfigs;
    }

    public void setFieldConfigs(List<FieldConfig> fieldConfigs) {
        this.fieldConfigs = fieldConfigs;
    }
}