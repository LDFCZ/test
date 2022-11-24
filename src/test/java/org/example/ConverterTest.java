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

        Collection<TreeDTO> target = new ArrayList<>();

        List<TreeDTO> children = new ArrayList<>();
        children.add(new TreeDTO(2, "2"));
        children.add(new TreeDTO(3, "3"));
        children.add(new TreeDTO(4, "4"));

        target.add(new TreeDTO(1, "1", children));

        Collection<TreeDTO> result = converter.convert(treeEntities);

        assertEquals(target.toString(), result.toString());
    }

    private List<TreeEntity> createTestTree() {
        List<TreeEntity> treeEntities = new ArrayList<>();
        // First component
        // Root
        treeEntities.add(new TreeEntity(1, "1", null));
        // Children
        treeEntities.add(new TreeEntity(2, "2", 1));
        treeEntities.add(new TreeEntity(3, "3", 1));
        treeEntities.add(new TreeEntity(4, "4", 1));

        // Second component
        // Root
        treeEntities.add(new TreeEntity(10, "10", null));
        // Children
        treeEntities.add(new TreeEntity(20, "20", 10));
        treeEntities.add(new TreeEntity(30, "30", 10));
        treeEntities.add(new TreeEntity(40, "40", 10));
        // Children's children
        treeEntities.add(new TreeEntity(50, "50", 20));
        treeEntities.add(new TreeEntity(60, "60", 20));
        treeEntities.add(new TreeEntity(70, "70", 40));
        return treeEntities;
    }

    private  Collection<TreeDTO> createTargetTree() {
        Collection<TreeDTO> target = new ArrayList<>();

        // First component
        // Children
        List<TreeDTO> children1 = new ArrayList<>();
        children1.add(new TreeDTO(2, "2"));
        children1.add(new TreeDTO(3, "3"));
        children1.add(new TreeDTO(4, "4"));
        // Root
        target.add(new TreeDTO(1, "1", children1));

        // Second component
        // Children's children
        List<TreeDTO> children20 = new ArrayList<>();
        children20.add(new TreeDTO(50, "50"));
        children20.add(new TreeDTO(60, "60"));

        List<TreeDTO> children40 = new ArrayList<>();
        children40.add(new TreeDTO(70, "70"));
        // Children
        List<TreeDTO> children10 = new ArrayList<>();
        children10.add(new TreeDTO(20, "20", children20));
        children10.add(new TreeDTO(30, "30"));
        children10.add(new TreeDTO(40, "40", children40));
        // Root
        target.add(new TreeDTO(10, "10", children10));

        return target;
    }

    @Test
    @DisplayName("Test with correct two-component tree")
    public void twoComponentTreeTest() {

        List<TreeEntity> treeEntities = this.createTestTree();

        Collection<TreeDTO> target = this.createTargetTree();

        Collection<TreeDTO> result = converter.convert(treeEntities);

        assertEquals(target.toString(), result.toString());
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

        Collection<TreeDTO> target = new ArrayList<>();
        TreeDTO previous = new TreeDTO(NODE_COUNT - 1, String.valueOf(NODE_COUNT - 1));

        for (int i = NODE_COUNT - 2; i >= 0; i--) {
            List<TreeDTO> children = new ArrayList<>();
            children.add(previous);
            previous = new TreeDTO(i, String.valueOf(i), children);
        }

        target.add(previous);

        Collection<TreeDTO> result = converter.convert(treeEntities);
        assertEquals(target.toString(), result.toString());
    }

    @Test
    @DisplayName("Empty input collection")
    public void emptyInputCollectionTest() {

        List<TreeEntity> treeEntities = new ArrayList<>();

        Collection<TreeDTO> target = new ArrayList<>();

        Collection<TreeDTO> result = converter.convert(treeEntities);

        assertEquals(target.toString(), result.toString());
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
    @DisplayName("Test exception on null as input")
    public void nullIdExceptionTest() {

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            converter.convert(null);
        });

        assertEquals("Null TreeEntity collection", thrown.getMessage());
    }

}