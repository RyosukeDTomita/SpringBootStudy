package com.example.study.service;

import com.example.study.domain.Order;
import com.example.study.domain.OrderRepository;
import com.example.study.domain.Product;
import com.example.study.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    // 固定時刻の Clock を使うことで、テストの結果が時刻に依存しない
    private final Clock fixedClock = Clock.fixed(
            Instant.parse("2026-04-25T10:00:00Z"),
            ZoneId.of("Asia/Tokyo")
    );

    private ShopService shopService;

    @BeforeEach
    void setUp() {
        shopService = new ShopService(productRepository, orderRepository, fixedClock);
    }

    @Test
    void getProduct_存在する商品を返す() {
        var product = new Product(1, "コーヒー", 500, 10);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Product result = shopService.getProduct(1);

        assertEquals("コーヒー", result.getName());
        assertEquals(500, result.getPrice());
    }

    @Test
    void getProduct_存在しない場合は例外を投げる() {
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> shopService.getProduct(999));
    }

    @Test
    void purchase_正常に購入できる() {
        var product = new Product(1, "コーヒー", 500, 10);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        shopService.purchase(1, 3);

        // 在庫を減らすメソッドが呼ばれたことを検証
        verify(productRepository).decrementStock(1, 3);

        // 注文が保存されたことを検証
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertEquals(1, savedOrder.getProductId());
        assertEquals(3, savedOrder.getQuantity());
        // 固定 Clock により時刻が確定的になる
        assertEquals(LocalDateTime.of(2026, 4, 25, 19, 0, 0), savedOrder.getOrderedAt());
    }

    @Test
    void purchase_在庫不足の場合は例外を投げる() {
        var product = new Product(1, "コーヒー", 500, 2);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> shopService.purchase(1, 5));
        assertTrue(ex.getMessage().contains("在庫が不足"));

        // 在庫の減少も注文の保存も呼ばれないことを検証
        verify(productRepository, never()).decrementStock(anyInt(), anyInt());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void purchase_存在しない商品の場合は例外を投げる() {
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> shopService.purchase(999, 1));

        verify(productRepository, never()).decrementStock(anyInt(), anyInt());
        verify(orderRepository, never()).save(any());
    }
}
