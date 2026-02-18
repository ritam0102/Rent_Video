package com.rentvideo.service;

import com.rentvideo.dto.VideoDto;
import com.rentvideo.entity.Video;
import com.rentvideo.exception.ResourceNotFoundException;
import com.rentvideo.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoService videoService;

    private Video buildVideo(Long id, boolean available) {
        return Video.builder()
                .id(id)
                .title("Inception")
                .director("Christopher Nolan")
                .genre("Sci-Fi")
                .available(available)
                .build();
    }

    @Test
    void getAllAvailableVideos_shouldReturnOnlyAvailable() {
        when(videoRepository.findByAvailableTrue()).thenReturn(List.of(buildVideo(1L, true)));

        List<VideoDto.VideoResponse> result = videoService.getAllAvailableVideos();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isAvailable());
    }

    @Test
    void getVideoById_shouldReturnVideo_whenFound() {
        when(videoRepository.findById(1L)).thenReturn(Optional.of(buildVideo(1L, true)));

        VideoDto.VideoResponse result = videoService.getVideoById(1L);

        assertEquals("Inception", result.getTitle());
        assertEquals("Christopher Nolan", result.getDirector());
    }

    @Test
    void getVideoById_shouldThrow_whenNotFound() {
        when(videoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> videoService.getVideoById(99L));
    }

    @Test
    void createVideo_shouldSaveAndReturn() {
        VideoDto.VideoRequest request = new VideoDto.VideoRequest();
        request.setTitle("The Matrix");
        request.setDirector("Wachowskis");
        request.setGenre("Action");
        request.setAvailable(true);

        Video saved = Video.builder().id(1L).title("The Matrix").director("Wachowskis").genre("Action").available(true).build();
        when(videoRepository.save(any(Video.class))).thenReturn(saved);

        VideoDto.VideoResponse result = videoService.createVideo(request);

        assertEquals("The Matrix", result.getTitle());
        verify(videoRepository).save(any(Video.class));
    }

    @Test
    void deleteVideo_shouldCallDelete_whenFound() {
        Video video = buildVideo(1L, true);
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        videoService.deleteVideo(1L);

        verify(videoRepository).delete(video);
    }

    @Test
    void deleteVideo_shouldThrow_whenNotFound() {
        when(videoRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> videoService.deleteVideo(5L));
    }

    @Test
    void updateVideo_shouldUpdateFields() {
        Video existing = buildVideo(1L, true);
        when(videoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(videoRepository.save(any(Video.class))).thenAnswer(inv -> inv.getArgument(0));

        VideoDto.VideoRequest request = new VideoDto.VideoRequest();
        request.setTitle("Updated Title");
        request.setDirector("New Director");
        request.setGenre("Drama");
        request.setAvailable(false);

        VideoDto.VideoResponse result = videoService.updateVideo(1L, request);

        assertEquals("Updated Title", result.getTitle());
        assertFalse(result.isAvailable());
    }
}
