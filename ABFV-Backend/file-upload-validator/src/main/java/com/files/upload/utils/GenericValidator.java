package com.files.upload.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.files.upload.model.Configuration;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GenericValidator {

    Logger logger = org.slf4j.LoggerFactory.getLogger(GenericValidator.class);

    private static final String VALIDATION_CONFIG_FILE = "validationConfig.json";


    public String validate(List<Map<String, Object>> dataList) throws IOException {
        // Load validation configuration
        String message = null;
        Configuration configuration = loadValidationConfig();
        int maxColumnCount = configuration.getMaxColumnCount();
        int maxFileSize = configuration.getMaxFileSize();
        List<FieldConfig> fieldConfigs = configuration.getFieldConfigs();
        Map<String, Set<String>> uniqueIdMaps = new HashMap<>();

        if(dataList.size()-1 > maxColumnCount) {
            message = "Validation Error: File exceeds the maximum number of records allowed.";
            return message;
        }
        for (FieldConfig fieldConfig : fieldConfigs) {
            if (fieldConfig.isRequired()) {
                if (!dataList.get(0).containsKey(fieldConfig.getName())) {
                    message = "Validation Error: Field '" + fieldConfig.getName() + "' is required.";
                    continue;
                }
            }
        }
        for (Map<String, Object> data : dataList) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                String fieldValue = String.valueOf(entry.getValue());
                
                Optional<FieldConfig> first = fieldConfigs.stream().filter(field ->
                        field.getName().equalsIgnoreCase(key)).findFirst();
                if (first.isPresent()) {
                    if (first.get().isRequired() && fieldValue.isEmpty()) {
                        // Field is required but is empty
                        message = "Validation Error: Field '" + first.get().getName() + "' is required.";
                    } else if (first.get().getMaxLength() != null && fieldValue.length() > first.get().getMaxLength()) {
                        // Field exceeds the maximum length allowed
                        message = "Validation Error: Field '" + first.get().getName() + "' exceeds the maximum length.";
                    } else if (!isDataTypeValid(fieldValue, first.get().getDataType())) {
                        // Field does not match the expected data type
                        message = "Validation Error: Field '" + first.get().getName() + "' has an invalid data type.";
                    } else if (first.get().getDataType().equals("date") && !isDateFormatValid(fieldValue, first.get().getDateFormat())) {
                        // Field does not match the expected date format
                        message = "Validation Error: Field '" + first.get().getName() + "' has an invalid date format.";
                    } else if (first.get().getPattern() != null && !fieldValue.matches(first.get().getPattern())) {
                        // Field does not match the expected pattern
                        message = "Validation Error: Field '" + first.get().getName() + "' has an invalid pattern.";
                    } else if (first.get().isUnique() && !isUnique(fieldValue, first.get().getName(), uniqueIdMaps)) {
                        // Field is not unique
                        message = "Validation Error: Field '" + first.get().getName() + "' is not unique. with value->" + fieldValue;
                    }

                    System.out.println("Key: " + key + ", Value: " + fieldValue);
                }
            }
            System.out.println();
        }
        if (message != null) {
            logger.error("File validation failed {}", message);
        } else {
            logger.info("File validation successful {}", message);
        }


        return message;
    }

    private boolean isUnique(String fieldValue, String name, Map<String, Set<String>> uniqueIdMaps) {
        if (uniqueIdMaps.containsKey(name) && uniqueIdMaps.get(name).contains(fieldValue)) {
            return false;
        } else {
            uniqueIdMaps.put(name, new HashSet<>());
        }
        uniqueIdMaps.get(name).add(fieldValue);
        return true;
    }

    public Configuration loadValidationConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Configuration configuration = objectMapper.readValue(new ClassPathResource(VALIDATION_CONFIG_FILE).getFile(), Configuration.class);
        return configuration;
}


    private boolean isDataTypeValid(String fieldValue, String dataType) {
        switch (dataType) {
            case "string":
                return true;
            case "integer":
                try {
                    Integer.parseInt(fieldValue);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case "double":
                try {
                    Double.parseDouble(fieldValue);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case "boolean":
                return fieldValue.equalsIgnoreCase("true") || fieldValue.equalsIgnoreCase("false");
            case "date":
                // Implement date validation logic according to the expected date format
                return true;
            default:
                return false;
        }
    }


    public static boolean isDateFormatValid(String dateStr, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        
        try {
            dateFormat.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}

