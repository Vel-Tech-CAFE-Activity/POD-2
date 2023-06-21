package com.bezkoder.spring.files.upload.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class CsvDuplicateRemover {

    public void removeDuplicates(MultipartFile multipartFile, Path outputFile) {
        Set<String> uniqueIds = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
             BufferedWriter writer = Files.newBufferedWriter(outputFile)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                String id = row[0]; // Assuming the ID is in the first column

                if (!uniqueIds.contains(id)) {
                    writer.write(line);
                    writer.newLine();

                    uniqueIds.add(id);
                }
            }

            if (uniqueIds.isEmpty()) {
                throw new IllegalStateException("No unique rows found in the input file.");
            }

            System.out.println("Duplicate rows removed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
