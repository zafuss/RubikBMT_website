package zafus.rubikbmt.rubikbmt_website.DTO;

import java.util.List;

public class CandidateDTO {
    private String fullName;
    private String phoneNumber;
    private List<EventDTO> events;

    public CandidateDTO() {}

    public CandidateDTO(String fullName, String phoneNumber, List<EventDTO> events) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.events = events;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }
}

