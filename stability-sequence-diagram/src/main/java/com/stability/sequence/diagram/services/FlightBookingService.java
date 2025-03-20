package com.stability.sequence.diagram.services;

import java.time.LocalDate;
import java.util.List;

import com.stability.sequence.diagram.data.Booking;
import com.stability.sequence.diagram.enums.BookingStatus;
import com.stability.sequence.diagram.repo.BookingDataRepo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FlightBookingService {

    @Resource
    private BookingDataRepo bookingDataRepo;

    public List<BookingDetails> getBookings() {
        return bookingDataRepo.query().getBookings().stream().map(this::toBookingDetails).toList();
    }

    private Booking findBooking(String bookingNumber, String name) {
        return bookingDataRepo.query().getBookings()
            .stream()
            .filter(b -> b.getBookingNumber().equalsIgnoreCase(bookingNumber))
            .filter(b -> b.getCustomer().getName().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public BookingDetails getBookingDetails(String bookingNumber, String name) {
        var booking = findBooking(bookingNumber, name);
        return toBookingDetails(booking);
    }

    public void changeBooking(String bookingNumber, String name, String newDate, String from, String to) {
        var booking = findBooking(bookingNumber, name);
        if (booking.getDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new IllegalArgumentException("Booking cannot be changed within 24 hours of the start date.");
        }
        booking.setDate(LocalDate.parse(newDate));
        booking.setFrom(from);
        booking.setTo(to);
    }

    public void cancelBooking(String bookingNumber, String name) {
        var booking = findBooking(bookingNumber, name);
        if (booking.getDate().isBefore(LocalDate.now().plusDays(2))) {
            throw new IllegalArgumentException("Booking cannot be cancelled within 48 hours of the start date.");
        }
        booking.setBookingStatus(BookingStatus.CANCELLED);
    }

    private BookingDetails toBookingDetails(Booking booking) {
        return new BookingDetails(booking.getBookingNumber(), booking.getCustomer().getName(), booking.getDate(),
            booking.getBookingStatus(), booking.getFrom(), booking.getTo(), booking.getBookingClass().toString());
    }

    public record BookingDetails(String bookingNumber, String name, LocalDate date, BookingStatus bookingStatus,
                                 String from, String to, String bookingClass) {
    }
}
