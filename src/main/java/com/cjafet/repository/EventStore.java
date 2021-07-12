package com.cjafet.repository;

import com.cjafet.data.Event;

import java.util.List;

public interface EventStore {
    void insert(Event event);

    void removeAll(String type);

    List<Event> query(String type, long startTime, long endTime);
}
