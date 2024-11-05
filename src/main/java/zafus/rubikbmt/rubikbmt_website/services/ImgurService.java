package zafus.rubikbmt.rubikbmt_website.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zafus.rubikbmt.rubikbmt_website.DTO.imgur.ImgurResponse;

import java.io.IOException;
import java.util.Base64;

@Service
public class ImgurService {

    @Value("${imgur.clientId}")
    private String clientId;

    @Value("${imgur.uploadUrl}")
    private String uploadUrl;

    public ImgurResponse uploadImage(MultipartFile file) throws IOException {
        byte[] imageBytes = file.getBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadRequest = new HttpPost(uploadUrl);
            uploadRequest.setHeader("Authorization", "Client-ID " + clientId);

            HttpEntity entity = MultipartEntityBuilder.create()
                    .addTextBody("image", base64Image)
                    .build();

            uploadRequest.setEntity(entity);

            HttpResponse response = httpClient.execute(uploadRequest);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            // Parse JSON response để lấy link ảnh
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonResponse, ImgurResponse.class);
        }
    }
}