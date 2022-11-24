package org.example;

import org.example.dto.TreeDTO;
import org.example.entity.TreeEntity;

import java.util.*;
public class Converter {

    // По идее было бы не плохо проверить на то, что некоторые ноды могут ссылаться друг на друга, в таком случае они не попадут
    // в результирующий список. Эту проверку можно сделать с помощью трассировки, как в алгоритме GC. Но в условиях задачи это не рассматривалось

    // Если TreeEntity будут браться не из БД, то так же нужно проверять на парные id и на id != null. В противном случае
    // одобные сущности нарушали бы условия целостности при хранении в БД.
    public Collection<TreeDTO> convert(Collection<TreeEntity> entities) {

        if (entities == null) {
            throw new RuntimeException("Null TreeEntity collection");
        }

        Map<Integer, TreeDTO> idTreeDTOMap = new HashMap<>();
        List<TreeDTO> result = new ArrayList<>();

        entities.forEach(entity -> {
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
