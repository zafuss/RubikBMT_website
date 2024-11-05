package zafus.rubikbmt.rubikbmt_website.DTO.imgur;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImgurResponse {

    @JsonProperty("status")
    private int status;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("data")
    private ImgurData data;

    // Getters và Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ImgurData getData() {
        return data;
    }

    public void setData(ImgurData data) {
        this.data = data;
    }
}