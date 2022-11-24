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

import static org.junit.jupiter.api.Assertions.*;

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

    public static final int NODE_COUNT = 1000;

    @Test
    @DisplayName("Linked list test with correct tree")
    public void linkedListCorrectTreeTest() {

        List<TreeEntity> treeEntities = new ArrayList<>();
        treeEntities.add(new TreeEntity(0, "0", null));

        for (int i = 1; i < NODE_COUNT; i++) {
            treeEntities.add(new TreeEntity(i, String.valueOf(i), i - 1));
        }

        Collection<TreeDTO> expectedCollection = new ArrayList<>();
        TreeDTO previous = new TreeDTO(NODE_COUNT - 1, String.valueOf(NODE_COUNT - 1));

        for (int i = NODE_COUNT - 2; i >= 0; i--) {
            List<TreeDTO> children = new ArrayList<>();
            children.add(previous);
            previous = new TreeDTO(i, String.valueOf(i), children);
        }

        expectedCollection.add(previous);


        Collection<TreeDTO> answerCollection = converter.convert(treeEntities);
        assertEquals(expectedCollection.toString(), answerCollection.toString());
    }


    @Test
    @DisplayName("Test exception on non-existent parent")
    public void nonExistentParentExceptionTest() {

        List<TreeEntity> treeEntities = new ArrayList<>();

        treeEntities.add(new TreeEntity(1, "1", null));
        treeEntities.add(new TreeEntity(2, "2", 1));
        treeEntities.add(new TreeEntity(3, "3", 10));
        treeEntities.add(new TreeEntity(4, "4", 1));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            converter.convert(treeEntities);
        });

        assertEquals("No parent with id - 10", thrown.getMessage());
    }

    @Test
    @DisplayName("Test exception on null id in entity")
    public void nullIdExceptionTest() {

        List<TreeEntity> treeEntities = new ArrayList<>();

        treeEntities.add(new TreeEntity(1, "1", null));
        treeEntities.add(new TreeEntity(2, "2", 1));
        treeEntities.add(new TreeEntity(null, "3", 1));
        treeEntities.add(new TreeEntity(4, "4", 1));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            converter.convert(treeEntities);
        });

        assertEquals("Null id in TreeEntity with name - 3", thrown.getMessage());
    }

    @Test
    @DisplayName("Test exception on same ids in entities")
    public void sameIdsExceptionTest() {

        List<TreeEntity> treeEntities = new ArrayList<>();

        treeEntities.add(new TreeEntity(1, "1", null));
        treeEntities.add(new TreeEntity(2, "2", 1));
        treeEntities.add(new TreeEntity(2, "3", 1));
        treeEntities.add(new TreeEntity(4, "4", 1));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            converter.convert(treeEntities);
        });

        assertEquals("There are more than one node with id - 2", thrown.getMessage());
    }

}