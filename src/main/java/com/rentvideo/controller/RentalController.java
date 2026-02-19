package com.rentvideo.controller;

import com.rentvideo.dto.RentalDto;
import com.rentvideo.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping("/{videoId}/rent")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<RentalDto> rentVideo(@PathVariable Long videoId, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(rentalService.rentVideo(email, videoId));
    }

    @PostMapping("/{videoId}/return")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<RentalDto> returnVideo(@PathVariable Long videoId, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(rentalService.returnVideo(email, videoId));
    }
}
