package com.rentvideo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class VideoDto {

    @Data
    public static class VideoRequest {
        @NotBlank
        private String title;

        @NotBlank
        private String director;

        @NotBlank
        private String genre;

        private boolean available = true;
    }

    @Data
    public static class VideoResponse {
        private Long id;
        private String title;
        private String director;
        private String genre;
        private boolean available;
    }
}
