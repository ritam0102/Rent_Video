package com.rentvideo.repository;

import com.rentvideo.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByAvailableTrue();
}
