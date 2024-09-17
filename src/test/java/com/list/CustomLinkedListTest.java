package com.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomLinkedListTest {

    CustomLinkedList<Integer> list;

    @BeforeEach
    void setup() {
        list = new CustomLinkedList<>();
    }

    @Test
    void testAddMethod() {
        list.add(1);
        boolean checkIfExists = list.contains(1);
        assertThat(checkIfExists).isTrue();
    }

    @Test
    void testGetMethod() {
        list.add(1);
        int temp = list.get(0);
        assertEquals(1, temp);
    }

    @Test
    void testRemoveMethod() {
        list.add(1);
        list.remove(0);
        boolean checkIfExists = list.contains(1);
        assertThat(checkIfExists).isFalse();
    }

    @Test
    void testAddAllMethod() {
        List<Integer> testList = new ArrayList<>();
        IntStream
                .range(0, 10)
                .boxed()
                .forEach(testList::add);
        list.addAll(testList);
        IntStream
                .range(0, 10)
                .boxed()
                .forEach(i -> assertThat(list.contains(testList.get(i))).isTrue());
        assertThat(list.size()).isEqualTo(testList.size());
    }

    @Test
    void testSizeMethod() {
        list.add(1);
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    void testReduceMethod() {
        list = IntStream
                .range(0, 10)
                .boxed()
                .reduce(new CustomLinkedList<>(), (customList, data) -> {
                            customList.add(data);
                            return customList;
                        },
                        (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        }
                );
        assertThat(list.size()).isEqualTo(10);
    }

}
