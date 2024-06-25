package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.entity.Order;
import com.kshrd.krorya.model.enumeration.PaymentMethodEnum;
import com.kshrd.krorya.model.request.OrderRequest;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.repository.PaymentRepository;
import com.kshrd.krorya.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import com.kshrd.krorya.repository.OrderRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/orders")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Order Controller", description = "Endpoint for managing carts")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @PostMapping("/orders")
    @Operation(summary = "Create order", description = "Create order")
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody OrderRequest orderRequest, @RequestParam PaymentMethodEnum paymentMethod) {
        Order order = orderService.createOrder(orderRequest, paymentMethod);
        return ResponseEntity.ok(
                ApiResponse.<Order>builder()
                        .message("order is created successfully!")
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .payload(order)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/orders")
    @Operation(summary = "Get all orders")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        List<Order> orderList = orderService.getAllOrders();
        return ResponseEntity.ok(
                ApiResponse.<List<Order>>builder()
                        .message("All orders are fetched successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(orderList)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get total of new orders")
    @GetMapping("/seller/order/total-new-order")
    public ResponseEntity<ApiResponse<Integer>> getTotalNewOrder(){
        Integer newOrder = orderRepository.countNewOrder();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Total of new orders is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(newOrder)
                        .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get total of success orders")
    @GetMapping("/seller/order/total-success-order")
    public ResponseEntity<ApiResponse<Integer>> getTotalSuccessOrder(){
        Integer successOrder = paymentRepository.countSuccessOrder();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Total of success orders is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(successOrder)
                        .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get total of cancel orders")
    @GetMapping("/seller/order/total-cancel-order")
    public ResponseEntity<ApiResponse<Integer>> getTotalCancleOrder(){
        Integer cancelOrder = orderRepository.countCancelOrder();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Total of cancel orders is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(cancelOrder)
                        .build()
        );
    }


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get total of payment ")
    @GetMapping("/seller/order/total-payment")
    public ResponseEntity<ApiResponse<Integer>> getTotalPayment(){
        Integer totalPayment = orderRepository.countTotalPayment();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Total of cancel orders is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(totalPayment)
                        .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get total number of preparing orders")
    @GetMapping("/seller/order/total-prepare-order")
    public ResponseEntity<ApiResponse<Integer>> getTotalPrepareOrders(){
        Integer totalPrepare = orderRepository.countTotalPrepareOrder();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Total of cancel orders is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(totalPrepare)
                        .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get total number of cooking orders")
    @GetMapping("/seller/order/total-cooking-order")
    public ResponseEntity<ApiResponse<Integer>> getTotalCookingOrders() {
        Integer totalCooking = orderRepository.countTotalCookingOrder();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Total of cooking orders is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(totalCooking)
                        .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get total number of cooked orders")
    @GetMapping("/seller/order/total-cooked-order")
    public ResponseEntity<ApiResponse<Integer>> getTotalCookedOrders() {
        Integer totalCooked = orderRepository.countTotalCookedOrder();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Total of cooked orders is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(totalCooked)
                        .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get total number of paid orders")
    @GetMapping("/seller/order/total-paid-order")
    public ResponseEntity<ApiResponse<Integer>> getTotalPaidOrder() {
        Integer totalPaidOrder = paymentRepository.countTotalPaidOrder();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Total of paid orders is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(totalPaidOrder)
                        .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get total number of unpaid orders")
    @GetMapping("/seller/order/total-unpaid-order")
    public ResponseEntity<ApiResponse<Integer>> getTotalUnPaidOrder() {
        Integer totalUnPaidOrder = paymentRepository.countTotalUnPaidOrder();
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .message("Total of unpaid orders is fetched successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(totalUnPaidOrder)
                        .build()
        );
    }




}
