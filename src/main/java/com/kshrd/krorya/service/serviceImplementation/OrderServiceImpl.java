package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.entity.Order;
import com.kshrd.krorya.model.entity.Payment;
import com.kshrd.krorya.model.enumeration.IsOrderRequestEnum;
import com.kshrd.krorya.model.enumeration.OrderStatusEnum;
import com.kshrd.krorya.model.enumeration.PaymentMethodEnum;
import com.kshrd.krorya.model.request.OrderRequest;
import com.kshrd.krorya.repository.OrderRepository;
import com.kshrd.krorya.repository.PaymentRepository;
import com.kshrd.krorya.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    private UUID getCurrentUserId() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAppUser().getUserId();
    }


    @Override
    public Order createOrder(OrderRequest orderRequest, PaymentMethodEnum paymentMethod) {
        Payment payment = paymentRepository.createPayment(getCurrentUserId(), paymentMethod);
        UUID paymentId = payment.getTransactionId();
        return orderRepository.createOrder(orderRequest, paymentId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }
}
