package com.cjafet.repository;

import com.cjafet.data.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class EventRepositoryTest {

    private static final String DRILLING = "drilling";
    private static final String DISTILLATION = "distillation";
    private static List<Event> events;
    private static Long ts = null;

//    @Autowired
//    TestEntityManager testEntityManager;

    @Autowired
    EventRepository eventRepository;

    @BeforeAll
    public static void setup() {
        ts = getTimestamp();
        events = Arrays.asList(
                new Event(DRILLING, ts),
                new Event(DISTILLATION, ts+250),
                new Event(DRILLING, ts+500),
                new Event(DISTILLATION, ts+750),
                new Event(DRILLING, ts+1000),
                new Event(DISTILLATION, ts+1250),
                new Event(DRILLING, ts+6500)
        );
    }

    @BeforeEach
    public void deleteEvents() {
        eventRepository.removeAll(DRILLING);
        eventRepository.removeAll(DISTILLATION);
    }

    @Test
    public void testWithConcurrency() throws InterruptedException {
        int numberOfThreads = 100000;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            service.execute(() -> {
                eventRepository.insert(new Event(DRILLING, getTimestamp()));
                latch.countDown();
            });
        }
        latch.await();
        Assertions.assertEquals(numberOfThreads, eventRepository.queryAll().size());
    }


    @Test
    public void testInsert() {
        eventRepository.insert(new Event(DRILLING, getTimestamp()));
        Assertions.assertEquals(1, eventRepository.queryAll().size());
    }

    @Test
    public void testInsertAll() {
        eventRepository.insertAll(events);
        Assertions.assertEquals(7, eventRepository.queryAll().size());
    }

    @Test
    public void testQuery() {
        eventRepository.insertAll(events);
        List<Event> events = eventRepository.query(DRILLING, ts, ts+5000);
        List<Event> allDrilling = eventRepository.query(DRILLING, ts, ts+7000);
        List<Event> allEvents = eventRepository.queryAll();

        Assertions.assertEquals(3, events.size());
        Assertions.assertEquals(4, allDrilling.size());
        Assertions.assertEquals(7, allEvents.size());
    }

    @Test
    public void testRemoveAll() {
        Integer size = eventRepository.queryAll().size();
        eventRepository.removeAll(DISTILLATION);
        eventRepository.removeAll(DRILLING);
        Assertions.assertEquals(0, eventRepository.queryAll().size());
    }

    private static Long getTimestamp() {
        return new Timestamp(System.currentTimeMillis()).getTime();
    }


}
