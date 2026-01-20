package com.example.booking.controller;

import com.example.booking.entity.Booking;
import com.example.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public Booking bookSeats(@RequestParam Long userId,
                             @RequestParam Long showId,
                             @RequestBody List<Long> seatIds) {

        return bookingService.createBooking(userId, showId, seatIds);
    }
}