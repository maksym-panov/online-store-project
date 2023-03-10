package com.panov.store.model;

import com.panov.store.utils.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "\"Order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unregisteredCustomerId")
    private UnregisteredCustomer unregisteredCustomer;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderProducts> orderProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "deliveryTypeId")
    private DeliveryType deliveryType;

    @NotNull
    @PastOrPresent
    private Timestamp postTime;
    @PastOrPresent
    private Timestamp completeTime;

    @NotNull
    @Convert(converter = Status.StatusConverter.class)
    private Status status;
}
