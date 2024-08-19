package com.example.techtask.service.impl;

import com.example.techtask.model.Order;
import com.example.techtask.model.enumiration.UserStatus;
import com.example.techtask.repository.OrderRepository;
import com.example.techtask.repository.UserRepository;
import com.example.techtask.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public Order findOrder() {
        return orderRepository.findAll()
                .stream()
                .filter(item -> item.getQuantity() > 1)
                .max(Comparator.comparing(Order::getCreatedAt))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Order> findOrders() {
        return userRepository.findAll()
                .stream()
                .filter(item -> item.getUserStatus().equals(UserStatus.ACTIVE))
                .flatMap(user -> user.getOrders().stream())
                .sorted(Comparator.comparing(Order::getCreatedAt))
                .toList();
    }
}
