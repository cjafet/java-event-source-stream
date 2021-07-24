package com.cjafet.repository;

import com.cjafet.data.Event;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EventRepository implements EventStore {
//    private Map<String, Event> store = new HashMap<>();
    private List<Event> store = new ArrayList<>();

    @Override
    public synchronized void insert(Event event) {
//        store.put(LocalDateTime.now().toString(), event);
        store.add(event);
    }

    public void insertAll(List<Event> events) {
        store.addAll(events);
    }

    @Override
    public void removeAll(String type) {
        List<Event> events = store.parallelStream()
                .filter(event -> event.type().equals(type))
                .collect(Collectors.toList());
        store.removeAll(events);
    }

    @Override
    public List<Event> query(String type, long startTime, long endTime) {
        return store.parallelStream()
                .filter(event -> (event.timestamp() >= startTime && event.timestamp() < endTime) && event.type().equals(type))
                .collect(Collectors.toList());

    }

    public List<Event> queryAll() {
        return store;
    }
}
