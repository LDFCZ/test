package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreeDTO {

    private Integer id;
    private String name;
    private List<TreeDTO> children;

    public TreeDTO() {

    }

    public TreeDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.children = new ArrayList<>();
    }

    public TreeDTO(Integer id, String name, List<TreeDTO> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }
}
