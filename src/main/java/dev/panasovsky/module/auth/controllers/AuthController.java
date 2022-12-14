package dev.panasovsky.module.auth.controllers;

import dev.panasovsky.module.auth.model.User;
import dev.panasovsky.module.auth.model.jwt.JWTRequest;
import dev.panasovsky.module.auth.services.UserService;
import dev.panasovsky.module.auth.services.AuthService;
import dev.panasovsky.module.auth.model.jwt.JWTResponse;
import dev.panasovsky.module.auth.model.jwt.RefreshJWTRequest;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;


    @PostMapping("register")
    public ResponseEntity<JsonNode> register(@RequestBody final User user) {

        final JsonNode result = userService.register(user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("login")
    public ResponseEntity<JWTResponse> login(
            @RequestBody final JWTRequest authRequest) {

        final JWTResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("token")
    public ResponseEntity<JWTResponse> getNewAccessToken(
            @RequestBody final RefreshJWTRequest request) {

        final JWTResponse accessToken = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(accessToken);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("refresh")
    public ResponseEntity<JWTResponse> getNewRefreshToken(
            @RequestBody final RefreshJWTRequest request) {

        final JWTResponse newRefreshToken = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(newRefreshToken);
    }

}