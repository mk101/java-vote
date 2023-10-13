package org.example.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVoteBody {

    private String topic;
    private String name;
    private String description;
    private List<String> answers;

}
