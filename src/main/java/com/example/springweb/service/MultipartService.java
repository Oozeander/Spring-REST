package com.example.springweb.service;

import com.example.springweb.exception.FileNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MultipartService {

    private static final Path multipartDirectory = Paths.get("uploads");
    private final ObjectMapper mapper;

    public Resource getFile(@NonNull String fileName) throws IOException {
        Path filePath = Paths.get(multipartDirectory.toString(), fileName);
        if (Files.notExists(filePath))
            throw new FileNotFoundException(fileName);
        return new ByteArrayResource(
                Files.readAllBytes(filePath)
        );
    }

    public String getFileNames() throws IOException {
        List<String> fileNames = Files.walk(multipartDirectory.toAbsolutePath(), 1).filter(Files::isRegularFile)
                .map(Path::getFileName).map(Path::toString).toList();
        var fileNamesJson = mapper.createObjectNode();
        var arrayNode = mapper.createArrayNode();
        fileNames.forEach(arrayNode::add);
        fileNamesJson.put("file_names", arrayNode);
        return mapper.writeValueAsString(fileNamesJson);
    }

    public void uploadFile(MultipartFile file) throws IOException {
        if (Files.notExists(multipartDirectory))
            Files.createDirectory(multipartDirectory);
        Files.write(Paths.get(multipartDirectory.toString(), file.getOriginalFilename()), file.getBytes());
    }
}
