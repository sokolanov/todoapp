package org.example.backend.dto;

import lombok.Data;

@Data
public class EditTaskDto {
    private String title;
    private String description;
    private boolean completed;
}
