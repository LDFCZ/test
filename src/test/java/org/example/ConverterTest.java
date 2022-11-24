package org.example;

import org.example.dto.TreeDTO;
import org.example.entity.TreeEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConverterTest {

    private static Converter converter;

    @BeforeAll
    static void setUp() {
        converter = new Converter();
    }

    @Test
    @DisplayName("Simple test with correct tree")
    public void simpleCorrectTreeTest() {

        List<TreeEntity> treeEntities = new ArrayList<>();

        treeEntities.add(new TreeEntity(1, "1", null));
        treeEntities.add(new TreeEntity(2, "2", 1));
        treeEntities.add(new TreeEntity(3, "3", 1));
        treeEntities.add(new TreeEntity(4, "4", 1));

        Collection<TreeDTO> expectedCollection = new ArrayList<>();

        List<TreeDTO> expectedChildren = new ArrayList<>();
        expectedChildren.add(new TreeDTO(2, "2"));
        expectedChildren.add(new TreeDTO(3, "3"));
        expectedChildren.add(new TreeDTO(4, "4"));
        expectedCollection.add(new TreeDTO(1, "1", expectedChildren));

        Collection<TreeDTO> answerCollection = converter.convert(treeEntities);

        assertEquals(expectedCollection.toString(), answerCollection.toString());
    }

}