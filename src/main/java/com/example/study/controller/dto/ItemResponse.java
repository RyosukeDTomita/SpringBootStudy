package com.example.study.controller.dto;

// @RestControllerのメソッド戻り値として返すと、HttpMessageConverter (MappingJackson2HttpMessageConverter) が
// このrecordを自動的にJSONにシリアライズしてレスポンスに書き込む
public record ItemResponse(Integer itemId, String name, Integer price, String description) {
}
