package com.rps.finwallet.controller;

import com.rps.finwallet.model.AppFile;
import com.rps.finwallet.repository.AppFileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final AppFileRepository appFileRepository;

    public FileUploadController(AppFileRepository appFileRepository) {
        this.appFileRepository = appFileRepository;
    }

    // Define the upload directory
    private static final String UPLOAD_DIR = "uploads/";

    // Endpoint for small files (Max 10MB) - Saves to DB and Filesystem
    @PostMapping("/upload/document")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Please select a file to upload."));
        }

        try {
            // Save physically
            String uniqueFileName = savePhysicalFile(file);

            // Save to DB as Base64
            String base64String = Base64.getEncoder().encodeToString(file.getBytes());
            AppFile appFile = new AppFile(file.getOriginalFilename(), file.getContentType(), base64String);
            appFileRepository.save(appFile);

            return ResponseEntity.ok(Map.of(
                    "message", "Document saved to database and filesystem!",
                    "fileName", uniqueFileName,
                    "databaseId", appFile.getId()
            ));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Could not upload document: " + e.getMessage()));
        }
    }

    // Endpoint for large files (Max 1GB) - Handles Chunk-by-Chunk Upload
    @PostMapping("/upload/video")
    public ResponseEntity<?> uploadVideo(
            @RequestParam("file") MultipartFile chunk,
            @RequestParam("fileName") String fileName,
            @RequestParam("chunkIndex") int chunkIndex,
            @RequestParam("totalChunks") int totalChunks,
            @RequestParam("uploadId") String uploadId) {
            
        if (chunk.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Empty chunk received."));
        }

        try {
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // We use the uploadId as the temporary file name while appending chunks
            Path tempFilePath = Paths.get(UPLOAD_DIR + uploadId + ".tmp");
            
            // Append the chunk bytes to the temp file
            Files.write(tempFilePath, chunk.getBytes(), 
                    java.nio.file.StandardOpenOption.CREATE, 
                    java.nio.file.StandardOpenOption.APPEND);

            // If this is the final chunk, rename the temp file to the final file name
            if (chunkIndex == totalChunks - 1) {
                String uniqueFileName = uploadId + "_" + fileName;
                Path finalFilePath = Paths.get(UPLOAD_DIR + uniqueFileName);
                Files.move(tempFilePath, finalFilePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                
                return ResponseEntity.ok(Map.of(
                        "message", "File completely uploaded and assembled!",
                        "fileName", uniqueFileName,
                        "status", "completed"
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Chunk " + (chunkIndex + 1) + "/" + totalChunks + " received successfully.",
                    "status", "uploading"
            ));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Error uploading chunk: " + e.getMessage()));
        }
    }

    // Helper method to save file to local directory
    private String savePhysicalFile(MultipartFile file) throws IOException {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;
        
        Path path = Paths.get(UPLOAD_DIR + uniqueFileName);
        Files.write(path, file.getBytes());
        
        return uniqueFileName;
    }
}
