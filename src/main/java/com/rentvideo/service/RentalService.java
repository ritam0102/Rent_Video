package com.rentvideo.service;

import com.rentvideo.dto.RentalDto;
import com.rentvideo.entity.Rental;
import com.rentvideo.entity.User;
import com.rentvideo.entity.Video;
import com.rentvideo.exception.BadRequestException;
import com.rentvideo.exception.ResourceNotFoundException;
import com.rentvideo.repository.RentalRepository;
import com.rentvideo.repository.UserRepository;
import com.rentvideo.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    @Transactional
    public RentalDto rentVideo(String email, Long videoId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));

        if (!video.isAvailable()) {
            throw new BadRequestException("Video is not available for rent");
        }

        long activeRentals = rentalRepository.countByUserAndActiveTrue(user);
        if (activeRentals >= 2) {
            throw new BadRequestException("Maximum 2 active rentals allowed");
        }

        Rental rental = Rental.builder()
                .user(user)
                .video(video)
                .rentedAt(LocalDateTime.now())
                .active(true)
                .build();

        // Mark video as unavailable (optional, but good practice if multiple copies aren't handled)
        // For this requirement, we assume we want to track it
        video.setAvailable(false);
        videoRepository.save(video);

        return toDto(rentalRepository.save(rental));
    }

    @Transactional
    public RentalDto returnVideo(String email, Long videoId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));

        Rental rental = rentalRepository.findByUserAndVideoAndActiveTrue(user, video)
                .orElseThrow(() -> new ResourceNotFoundException("Active rental not found for this video and user"));

        rental.setReturnedAt(LocalDateTime.now());
        rental.setActive(false);

        video.setAvailable(true);
        videoRepository.save(video);

        return toDto(rentalRepository.save(rental));
    }

    private RentalDto toDto(Rental rental) {
        RentalDto dto = new RentalDto();
        dto.setId(rental.getId());
        dto.setUserEmail(rental.getUser().getEmail());
        dto.setVideoTitle(rental.getVideo().getTitle());
        dto.setRentedAt(rental.getRentedAt());
        dto.setReturnedAt(rental.getReturnedAt());
        dto.setActive(rental.isActive());
        return dto;
    }
}
