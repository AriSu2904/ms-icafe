package com.atm.inet.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailRequest {

    private String customerId;
    private String computerId;
    private Integer duration;
    private LocalDateTime bookingDate;

    @JsonSetter("bookingDate")
    public void setBookingDate(String bookingDate) {
        try {
            this.bookingDate = LocalDateTime.parse(bookingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            this.bookingDate = LocalDateTime.parse(bookingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        }
    }

}
