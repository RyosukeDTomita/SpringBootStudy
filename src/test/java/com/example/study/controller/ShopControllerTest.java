package com.example.study.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.study.config.AppConfig;
import com.example.study.config.SecurityConfig;
import com.example.study.converter.ShopConverterImpl;
import com.example.study.domain.Product;
import com.example.study.service.ShopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// /shop/** は SecurityConfig で .authenticated() のため、認証済みユーザでないとアクセスできない。
@WebMvcTest(ShopController.class)
@Import({AppConfig.class, SecurityConfig.class, ShopConverterImpl.class})
class ShopControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ShopService shopService;

  @Test
  @WithMockUser
  void shopPage_認証済みユーザはshopページを表示() throws Exception {
    when(shopService.getProduct(1)).thenReturn(new Product(1, "りんご", 150, 5));

    mockMvc
        .perform(get("/shop"))
        .andExpect(status().isOk())
        .andExpect(view().name("shop/index"))
        .andExpect(model().attributeExists("product"));
  }

  @Test
  void shopPage_未認証はログイン画面へリダイレクト() throws Exception {
    mockMvc.perform(get("/shop")).andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser
  void purchase_成功時はsuccessMessageがモデルに入る() throws Exception {
    when(shopService.getProduct(1)).thenReturn(new Product(1, "りんご", 150, 3));

    mockMvc
        .perform(
            post("/shop/purchase")
                .with(csrf())
                .param("productId", "1")
                .param("quantity", "2"))
        .andExpect(status().isOk())
        .andExpect(view().name("shop/index"))
        .andExpect(model().attributeExists("successMessage"))
        .andExpect(model().attributeDoesNotExist("errorMessage"));
  }

  @Test
  @WithMockUser
  void purchase_例外時はerrorMessageがモデルに入る() throws Exception {
    doThrow(new RuntimeException("在庫が不足しています")).when(shopService).purchase(1, 99);
    when(shopService.getProduct(1)).thenReturn(new Product(1, "りんご", 150, 5));

    mockMvc
        .perform(
            post("/shop/purchase")
                .with(csrf())
                .param("productId", "1")
                .param("quantity", "99"))
        .andExpect(status().isOk())
        .andExpect(view().name("shop/index"))
        .andExpect(model().attributeExists("errorMessage"));
  }
}
