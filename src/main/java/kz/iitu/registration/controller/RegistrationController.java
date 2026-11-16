package kz.iitu.registration.controller;

import kz.iitu.registration.model.EventInfo;
import kz.iitu.registration.service.EventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    private final EventService eventService;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${app.course}")
    private String course;

    @Value("${app.semester}")
    private int semester;

    public RegistrationController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", eventService.getWelcomeMessage());
    }

    @GetMapping("/events")
    public List<EventInfo> getEvents(
            @RequestParam(name = "title", required = false) String title) {
        return eventService.getEvents(title);
    }

    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        return Map.of(
                "applicationName", applicationName,
                "course", course,
                "semester", semester
        );
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<?> getEventById(@PathVariable("id") Long id) {
        EventInfo event = eventService.getEventById(id);

        if (event == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Event not found", "id", id));


        return ResponseEntity.ok(event);
    }

    @PostMapping("/events")
    public ResponseEntity<?> addEvent(@RequestBody EventInfo request) {
        if (hasBlankFields(request))
            return ResponseEntity.badRequest().body(Map.of("message", "Title and location are required"));

        EventInfo event = eventService.addEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<?> updateEvent(
            @PathVariable("id") Long id,
            @RequestBody EventInfo request) {

        if (hasBlankFields(request))
            return ResponseEntity.badRequest().body(Map.of("message", "Title and location are required"));

        EventInfo event = eventService.updateEvent(id, request);

        if (event == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Event not found", "id", id));

        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable("id") Long id) {
        boolean deleted = eventService.deleteEvent(id);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Event not found", "id", id));
        }

        return ResponseEntity.noContent().build();
    }

    private boolean hasBlankFields(EventInfo event) {
        return (event.getTitle() == null || event.getTitle().isBlank()) || (event.getLocation() == null || event.getLocation().isBlank());
    }
}


