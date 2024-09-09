package com.list;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

@Slf4j
public class Main {
    private static CustomLinkedList<Integer> list = new CustomLinkedList<>();

    private static final String CURRENT_LIST_TEXT = "Current list: {}";

    public static void main(String[] args) {
        list.add(1);
        log.debug("Added 1 element to list");
        log.info(CURRENT_LIST_TEXT, list.toString());


        boolean contains = list.contains(1);
        log.info("Does current list contains this element? {}", contains);

        int number = list.get(0);
        log.debug("Current list contains the element: {}", number);

        list.remove(0);
        log.debug("Removed 1 element from list");
        log.info(CURRENT_LIST_TEXT, list.toString());

        int size = list.size();
        log.debug("Current list size is {}", size);

        list.addAll(IntStream.range(0, 20).boxed().toList());

        log.debug("Added 20 elements to list");
        log.info(CURRENT_LIST_TEXT, list.toString());

        list = IntStream.range(0, 20).boxed().reduce(new CustomLinkedList<>(), (customList, data) -> {
            customList.add(data);
            return customList;
        }, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
        log.debug("Added 20 more elements to list");
        log.info(CURRENT_LIST_TEXT, list.toString());
    }
}
