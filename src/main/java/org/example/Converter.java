package org.example;

import org.example.dto.TreeDTO;
import org.example.entity.TreeEntity;

import java.util.*;
public class Converter {

    // По идее было бы не плохо проверить на то, что некоторые ноды могут ссылаться друг на друга, в таком случае они не попадут
    // в результирующий список. Эту проверку можно сделать с помощью трассировки, как в алгоритме GC.
    public Collection<TreeDTO> convert(Collection<TreeEntity> entities) {

        Map<Integer, TreeDTO> idTreeDTOMap = new HashMap<>();
        List<TreeDTO> result = new ArrayList<>();

        entities.forEach(entity -> {
            if (entity.getId() == null) {
                throw new RuntimeException("Null id in TreeEntity with name - " + entity.getName());
            }
            if (idTreeDTOMap.containsKey(entity.getId())) {
                throw new RuntimeException("There are more than one node with id - " + entity.getParentId());
            }

            TreeDTO treeDTO = new TreeDTO(entity.getId(), entity.getName());
            idTreeDTOMap.put(entity.getId(), treeDTO);

            if (entity.getParentId() == null) {
                result.add(treeDTO);
            }
        });

        entities.forEach(entity -> {
            if (idTreeDTOMap.containsKey(entity.getParentId())) {
                List<TreeDTO> children = idTreeDTOMap.get(entity.getParentId()).getChildren();
                children.add(idTreeDTOMap.get(entity.getId()));
            } else if (entity.getParentId() != null) {
                throw new RuntimeException("No parent with id - " + entity.getParentId());
            }
        });

        return result;
    }

}
