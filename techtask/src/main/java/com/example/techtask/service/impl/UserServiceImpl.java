package com.example.techtask.service.impl;

import com.example.techtask.model.Order;
import com.example.techtask.model.User;
import com.example.techtask.model.enumiration.OrderStatus;
import com.example.techtask.repository.OrderRepository;
import com.example.techtask.repository.UserRepository;
import com.example.techtask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    public User findUser() {
        return userRepository.findAll()
                .stream()
                .max(Comparator.comparing(user -> user.getOrders()
                        .stream()
                        .filter(item -> item.getCreatedAt().getYear() == 2003 && item.getOrderStatus().equals(OrderStatus.DELIVERED))
                        .mapToDouble(Order::getPrice)
                        .sum()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<User> findUsers() {
        return userRepository
                .findAllById(orderRepository
                .findAll()
                .stream()
                .filter(order -> order.getCreatedAt().getYear() == 2010 && order.getOrderStatus().equals(OrderStatus.PAID))
                        .map(Order::getUserId)
                        .distinct()
                        .toList());
    }
}
