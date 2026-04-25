package com.example.study.controller;

import com.example.study.converter.ShopConverter;
import com.example.study.domain.Product;
import com.example.study.service.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shop")
public class ShopController {
  private final ShopService shopService;
  private final ShopConverter shopConverter;

  public ShopController(ShopService shopService, ShopConverter shopConverter) {
    this.shopService = shopService;
    this.shopConverter = shopConverter;
  }

  @GetMapping
  public String shopPage(Model model) {
    Product product = shopService.getProduct(1);
    model.addAttribute("product", shopConverter.toResponse(product));
    return "shop/index";
  }

  @PostMapping("/purchase")
  public String purchase(
      @RequestParam Integer productId, @RequestParam Integer quantity, Model model) {
    try {
      shopService.purchase(productId, quantity);
      model.addAttribute("successMessage", quantity + "個購入しました！");
    } catch (RuntimeException e) {
      model.addAttribute("errorMessage", e.getMessage());
    }

    Product product = shopService.getProduct(productId);
    model.addAttribute("product", shopConverter.toResponse(product));
    return "shop/index";
  }
}
