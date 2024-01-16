package com.atm.inet.entity;


import com.atm.inet.entity.computer.Computer;
import com.atm.inet.entity.computer.TypePrice;
import com.atm.inet.entity.constant.EStatusOrder;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_order_detail")
public class OrderDetail {

    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Id
    private String id;

    @ManyToOne(targetEntity = Customer.class)
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    @ManyToOne(targetEntity = Computer.class)
    @JoinColumn(name = "computer_id")
    @JsonBackReference
    private Computer computer;

    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "type_price_id")
    private TypePrice typePrice;

    @Column(name = "start_booking")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bookingDate;

    @Column(name = "end_booking")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endBookingDate;

    @Column(name = "transaction_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private EStatusOrder status;

}
