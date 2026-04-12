package com.example.study.controller.dto;

// 実装クラスは自動で作成される。今回は削りたいフィールドなどはないので、MapStructを使い、フィールドはUserクラスと同じものを定義する。
// NOTE: DTOを作ることでDBカラムいぇEntityクラスの変更がAPIに影響しないようにしたり、逆にAPIの変更がDBに影響しないようにすることができる。
public record UserResponse(String userId, String profile) {
}
