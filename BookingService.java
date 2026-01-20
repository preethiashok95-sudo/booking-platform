package com.example.booking.service;

import com.example.booking.entity.Booking;
import com.example.booking.entity.Seat;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatLockService seatLockService;

    @Transactional
    public Booking createBooking(Long userId, Long showId, List<Long> seatIds) {

        for (Long seatId : seatIds) {
            boolean locked = seatLockService.lockSeat(showId, seatId, userId);
            if (!locked) {
                throw new RuntimeException("Seat already locked by another user");
            }
        }

        List<Seat> seats = seatRepository.findAllById(seatIds);
        for (Seat seat : seats) {
            if (seat.isBooked()) {
                throw new RuntimeException("Seat already booked");
            }
            seat.setBooked(true);
        }

        seatRepository.saveAll(seats);

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setShowId(showId);
        booking.setStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }
}