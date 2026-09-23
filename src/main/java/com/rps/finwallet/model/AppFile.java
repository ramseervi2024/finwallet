package com.rps.finwallet.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_files")
public class AppFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String base64Content;

    public AppFile() {
    }

    public AppFile(String fileName, String fileType, String base64Content) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.base64Content = base64Content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getBase64Content() {
        return base64Content;
    }

    public void setBase64Content(String base64Content) {
        this.base64Content = base64Content;
    }
}
