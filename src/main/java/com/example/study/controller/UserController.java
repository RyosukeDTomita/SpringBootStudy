package com.example.study.controller;

import com.example.study.controller.dto.UserResponse;
import com.example.study.controller.dto.UserSearchRequest;
import com.example.study.converter.UserConverter;
import com.example.study.domain.User;
import com.example.study.service.UserService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final UserConverter userConverter;

  public UserController(UserService userService, UserConverter userConverter) {
    this.userService = userService;
    this.userConverter = userConverter;
  }

  // NOTE: /users だけならフォーム表示、/users?userId=hoge ならバリデーション後にプロフィールへリダイレクト
  // NOTE: Bean Validationで空文字・null を弾く。@Validでバリデーションを実行し、BindingResultでエラーを受け取る。
  @GetMapping
  public String search(
      @Valid @ModelAttribute("searchRequest") UserSearchRequest searchRequest,
      BindingResult bindingResult,
      Model model) {
    // userIdパラメータがない場合はフォーム表示のみ
    if (searchRequest.userId() == null) {
      return "user/search";
    }
    if (bindingResult.hasErrors()) {
      return "user/search";
    }
    return "redirect:/users/" + searchRequest.userId();
  }

  @GetMapping("/{userId}")
  // NOTE:@PathVariableを使うとurlは/profile/123のようになる。@RequestParamを使うと/profile?userId=123のような形式になる。
  public String showProfile(@PathVariable String userId, Model model) {
    Optional<User> user = userService.findByUserId(userId);
    if (user.isEmpty()) {
      model.addAttribute("errorMessage", "ユーザーが見つかりませんでした: " + userId);
      model.addAttribute("searchRequest", new UserSearchRequest(null));
      return "user/search";
    }
    UserResponse response = userConverter.toResponse(user.get());
    model.addAttribute("user", response); // th:text="${user.userId}でThymeleafから引ける
    return "user/profile";
  }
}
