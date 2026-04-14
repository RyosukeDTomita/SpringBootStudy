package com.example.study.service;

import com.example.study.domain.Order;
import com.example.study.domain.OrderRepository;
import com.example.study.domain.Product;
import com.example.study.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class ShopService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final Clock clock;

    // @Autowired // コンストラクタ1つの場合は省略可能
    public ShopService(ProductRepository productRepository, OrderRepository orderRepository, Clock clock) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.clock = clock;
    }

    public Product getProduct(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品が見つかりません"));
    }

    /**
     * 購入処理。
     * @Transactional により在庫の減少と注文の記録が1つのトランザクションで実行される。
     * RuntimeException（非検査例外）が発生すると、すべてのDB操作が自動的にロールバックされる。
     * NOTE: @Transactionalのメソッドが成功したら、DBへの変更は自動的にコミットされる。例外が発生した場合はロールバックされる。
     */
    @Transactional
    public void purchase(Integer productId, Integer quantity) {
        // 1. 在庫を確認する
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品が見つかりません"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("在庫が不足しています（残り: " + product.getStock() + "個）");
        }

        // 2. 在庫を減らす（1つ目のDB書き込み）
        productRepository.decrementStock(productId, quantity);

        // 3. 注文を記録する（2つ目のDB書き込み）
        // @Transactional がなければ、ここで例外が起きた場合に2だけ反映されてしまう
        Order order = new Order(null, productId, quantity, LocalDateTime.now(clock));
        orderRepository.save(order);
    }
}
