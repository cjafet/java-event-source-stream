package com.cjafet;

import com.cjafet.data.Event;
import com.cjafet.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication()
public class EventSourceApplication implements CommandLineRunner {

    public static final String DRILLING = "drilling";
    public static final String DISTILLATION = "distillation";

    public static void main(String[] args) {
        SpringApplication.run(EventSourceApplication.class, args);
    }

    @Bean
    public String args() {
        return "Start drilling";
    }

    @Autowired
    private EventService eventService;

    @Override
    public void run(String... args) {

        Long ts = new Timestamp(System.currentTimeMillis()).getTime();

        List<Event> events = Arrays.asList(
                new Event(DRILLING, ts),
                new Event(DISTILLATION, ts+250)
        );

        eventService.addEvents(events);
        eventService.addEvent(new Event(DISTILLATION, ts+6000));

        List<Event> drilling = eventService.findEvents(DRILLING, ts, ts+5000);
        printEvents(drilling);

        List<Event> distilling = eventService.findEvents(DISTILLATION, ts, ts+5000);
        printEvents(distilling);

        eventService.removeAllByType(DISTILLATION);
        eventService.removeAllByType(DRILLING);
        System.out.println(eventService.findAll());

    }

    public void printEvents(List<Event> events) {
        System.out.println("------------------------------");
        System.out.println(events.size());
        System.out.println(events);
        System.out.printf("%s %s %n", events.get(0).type(), events.get(0).timestamp());
        System.out.println("------------------------------");
    }
}
