package kz.iitu.registration.service;

import kz.iitu.registration.model.EventInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class EventService {

    private final List<EventInfo> events = List.of(
            new EventInfo(1L, "Web Design Workshop", "Room 101"),
            new EventInfo(2L, "JavaScript Quiz", "Room 202"),
            new EventInfo(3L, "IT Career Talk", "Conference Hall")
    );

    private long nextId = 4;

    public String getWelcomeMessage() {
        return "Welcome to the Event Registration API";
    }

    public synchronized List<EventInfo> getEvents() {
        return new ArrayList<>(events);
    }

    public synchronized List<EventInfo> getEvents(String title) {
        if (title == null)
            return getEvents();


        String query = title.toLowerCase(Locale.ROOT);
        List<EventInfo> result = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            EventInfo event = events.get(i);

            if (event.getTitle().toLowerCase(Locale.ROOT).contains(query))
                result.add(event);
        }

        return result;
    }

    public synchronized EventInfo getEventById(Long id) {
        for (int i = 0; i < events.size(); i++) {
            EventInfo event = events.get(i);

            if (event.getId().equals(id))
                return event;
        }

        return null;
    }

    public synchronized EventInfo addEvent(EventInfo request) {
        EventInfo event = new EventInfo(nextId++, request.getTitle(), request.getLocation());
        events.add(event);
        return event;
    }

    public synchronized EventInfo updateEvent(Long id, EventInfo request) {
        for (int i = 0; i < events.size(); i++) {
            EventInfo event = events.get(i);

            if (event.getId().equals(id)) {
                EventInfo updated = new EventInfo(id, request.getTitle(), request.getLocation());
                events.set(i, updated);
                return updated;
            }
        }
        return null;
    }

    public synchronized boolean deleteEvent(Long id) {
        EventInfo event = getEventById(id);

        if (event == null)
            return false;

        events.remove(event);
        return true;
    }
}