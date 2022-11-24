package org.example.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TreeEntity {

    private Integer id;

    private String name;

    private Integer parentId;

    public TreeEntity() {

    }

    public TreeEntity(Integer id, String name, Integer parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

}
