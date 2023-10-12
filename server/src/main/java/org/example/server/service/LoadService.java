package org.example.server.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.server.model.Topic;
import org.example.server.repository.SaveLoadRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
public class LoadService {

    private final SaveLoadRepository repository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void load(String path) {
        Path filePath = Path.of(path);
        List<Topic> topics = objectMapper.readValue(Files.readAllBytes(filePath), new TypeReference<List<Topic>>() {});
        repository.update(topics);
    }

}
