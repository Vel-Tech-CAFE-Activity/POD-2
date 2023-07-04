package com.files.upload.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;


@Entity
@Table(name = "files")
public class FileData {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob()
    private String jsonData;

    @Column
    private String name;

    @Column
    private String hash;
    
    @Column(name="file_name")
    private String fileName;

    // Getters and Setters


    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
    
   
}



