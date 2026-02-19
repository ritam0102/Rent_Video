package com.rentvideo.repository;

import com.rentvideo.entity.Rental;
import com.rentvideo.entity.User;
import com.rentvideo.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    long countByUserAndActiveTrue(User user);
    Optional<Rental> findByUserAndVideoAndActiveTrue(User user, Video video);
}
