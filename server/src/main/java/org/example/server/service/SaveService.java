package org.example.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.server.repository.SaveLoadRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@RequiredArgsConstructor
public class SaveService {

    private final SaveLoadRepository repository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void save(String path) {
        Path filePath = Path.of(path);
        Files.writeString(filePath, objectMapper.writeValueAsString(repository.getTopics()),
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }

}
