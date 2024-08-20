package zafus.rubikbmt.rubikbmt_website.DTO;

public class EventDTO {
    private String name;

    // Constructors
    public EventDTO() {}

    public EventDTO(String name) {
        this.name = name;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}