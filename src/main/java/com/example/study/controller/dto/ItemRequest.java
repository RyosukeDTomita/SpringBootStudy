package com.example.study.controller.dto;

// @RequestBodyでJSONを受け取る際に、HttpMessageConverter (MappingJackson2HttpMessageConverter) が
// JSONをこのrecordに自動変換する
public record ItemRequest(String name, Integer price, String description) {
}
