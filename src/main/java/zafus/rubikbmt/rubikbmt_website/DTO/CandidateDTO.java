package zafus.rubikbmt.rubikbmt_website.DTO;

import java.util.List;

public class CandidateDTO {
    private String fullName;
    private List<EventDTO> events;

    public CandidateDTO() {}

    public CandidateDTO(String fullName, List<EventDTO> events) {
        this.fullName = fullName;
        this.events = events;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }
}

