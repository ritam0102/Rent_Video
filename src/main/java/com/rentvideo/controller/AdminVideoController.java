package com.rentvideo.controller;

import com.rentvideo.entity.Video;
import com.rentvideo.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/videos")
@PreAuthorize("hasRole('ADMIN')")
public class AdminVideoController {

    @Autowired
    private VideoRepository videoRepository;

    @PostMapping
    public Video addVideo(@RequestBody Video video) {
        return videoRepository.save(video);
    }

    @PutMapping("/{id}")
    public Video updateVideo(@PathVariable Long id, @RequestBody Video v) {
        Video video = videoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found"));

        video.setTitle(v.getTitle());
        video.setDirector(v.getDirector());
        video.setGenre(v.getGenre());
        return videoRepository.save(video);
    }

    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable Long id) {
        videoRepository.deleteById(id);
    }
}
