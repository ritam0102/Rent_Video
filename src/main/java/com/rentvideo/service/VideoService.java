package com.rentvideo.service;

import com.rentvideo.dto.VideoDto;
import com.rentvideo.entity.Video;
import com.rentvideo.exception.ResourceNotFoundException;
import com.rentvideo.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

    public List<VideoDto.VideoResponse> getAllAvailableVideos() {
        return videoRepository.findByAvailableTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<VideoDto.VideoResponse> getAllVideos() {
        return videoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public VideoDto.VideoResponse getVideoById(Long id) {
        Video video = findById(id);
        return toResponse(video);
    }

    public VideoDto.VideoResponse createVideo(VideoDto.VideoRequest request) {
        Video video = Video.builder()
                .title(request.getTitle())
                .director(request.getDirector())
                .genre(request.getGenre())
                .available(request.isAvailable())
                .build();
        return toResponse(videoRepository.save(video));
    }

    public VideoDto.VideoResponse updateVideo(Long id, VideoDto.VideoRequest request) {
        Video video = findById(id);
        video.setTitle(request.getTitle());
        video.setDirector(request.getDirector());
        video.setGenre(request.getGenre());
        video.setAvailable(request.isAvailable());
        return toResponse(videoRepository.save(video));
    }

    public void deleteVideo(Long id) {
        Video video = findById(id);
        videoRepository.delete(video);
    }

    private Video findById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id));
    }

    private VideoDto.VideoResponse toResponse(Video video) {
        VideoDto.VideoResponse response = new VideoDto.VideoResponse();
        response.setId(video.getId());
        response.setTitle(video.getTitle());
        response.setDirector(video.getDirector());
        response.setGenre(video.getGenre());
        response.setAvailable(video.isAvailable());
        return response;
    }
}
