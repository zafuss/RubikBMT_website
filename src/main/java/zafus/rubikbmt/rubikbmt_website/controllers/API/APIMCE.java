package zafus.rubikbmt.rubikbmt_website.controllers.API;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIMCE {

    @Value("${tinymce.api.key}")
    private String tinymceApiKey;

    @GetMapping("/api/tinymce-key")
    public String getTinyMceApiKey() {
        return tinymceApiKey;
    }
}