package zafus.rubikbmt.rubikbmt_website.DTO.imgur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgurData {

    @JsonProperty("id")
    private String id;

    @JsonProperty("link")
    private String link;

    // Các field khác bạn có thể thêm nếu cần

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}