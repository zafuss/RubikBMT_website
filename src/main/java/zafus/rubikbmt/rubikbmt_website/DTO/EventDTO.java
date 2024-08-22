package zafus.rubikbmt.rubikbmt_website.DTO;

public class EventDTO {
    private String name;
    private String imageURL;
    // Constructors
    public EventDTO() {}

    public EventDTO(String name,String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}