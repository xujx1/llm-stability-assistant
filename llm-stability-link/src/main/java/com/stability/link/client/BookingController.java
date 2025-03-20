package com.stability.link.client;

import java.util.List;

import com.stability.link.services.FlightBookingService;
import com.stability.link.services.FlightBookingService.BookingDetails;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Resource
    private FlightBookingService flightBookingService;

    @RequestMapping("/bookings")
    @ResponseBody
    public List<BookingDetails> getBookings() {
        return flightBookingService.getBookings();
    }
}
