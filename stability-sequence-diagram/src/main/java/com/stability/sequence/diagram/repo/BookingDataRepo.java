package com.stability.sequence.diagram.repo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.stability.sequence.diagram.data.*;
import com.stability.sequence.diagram.enums.*;
import org.springframework.stereotype.Repository;

@Repository
public class BookingDataRepo {

    public BookingData query() {
        BookingData data = new BookingData();
        List<String> names = List.of("云小宝", "李千问", "张百炼", "王通义", "刘魔搭");
        List<String> airportCodes = List.of("北京", "上海", "广州", "深圳", "杭州", "南京", "青岛", "成都", "武汉",
            "西安", "重庆", "大连",
            "天津");
        Random random = new Random();

        var customers = new ArrayList<Customer>();
        var bookings = new ArrayList<Booking>();

        for (int i = 0; i < 5; i++) {
            String name = names.get(i);
            String from = airportCodes.get(random.nextInt(airportCodes.size()));
            String to = airportCodes.get(random.nextInt(airportCodes.size()));
            BookingClass bookingClass = BookingClass.values()[random.nextInt(BookingClass.values().length)];
            Customer customer = new Customer();
            customer.setName(name);

            LocalDate date = LocalDate.now().plusDays(2 * (i + 1));

            Booking booking = new Booking("10" + (i + 1), date, customer, BookingStatus.CONFIRMED, from, to,
                bookingClass);
            customer.getBookings().add(booking);

            customers.add(customer);
            bookings.add(booking);
        }

        // Reset the database on each start
        data.setCustomers(customers);
        data.setBookings(bookings);
        return data;
    }
}
