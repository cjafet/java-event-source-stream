package com.cjafet.service;

import com.cjafet.data.Event;
import com.cjafet.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public void addEvent(Event event) {
        eventRepository.insert(event);
    }

    public void addEvents(List<Event> events) {
        eventRepository.insertAll(events);
    }

    public List<Event> findEvents(String type, long startTime, long endTime) {
        return eventRepository.query(type, startTime, endTime);
    }

    public List<Event> findAll() {
        return eventRepository.queryAll();
    }

    public void removeAllByType(String type) {
        eventRepository.removeAll(type);
    }

}
