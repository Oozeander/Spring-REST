package com.example.springweb.endpoint;

import com.example.springweb.service.MultipartService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class MultipartController {

    private final MultipartService multipartService;

    @GetMapping(value = "{fileName:.+}", produces = {
            MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.APPLICATION_PDF_VALUE}) // APPLICATION_OCTET_STREAM_VALUE
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> getFile(
            @PathVariable("fileName") String fileName
    ) throws IOException {
        Resource file = multipartService.getFile(fileName);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> postFile(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        multipartService.uploadFile(file);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{fileName}")
                        .buildAndExpand(file.getOriginalFilename())
                        .toUri()
        ).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getFileNames() throws IOException {
        return ResponseEntity.ok(multipartService.getFileNames());
    }
}
