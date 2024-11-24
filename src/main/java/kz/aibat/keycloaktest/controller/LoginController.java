package kz.aibat.keycloaktest.controller;

import kz.aibat.keycloaktest.dto.UserLoginDTO;
import kz.aibat.keycloaktest.dto.UserTokenDTO;
import kz.aibat.keycloaktest.service.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login")
@RequiredArgsConstructor
public class LoginController {

    private final KeycloakUserService keycloakUserService;

    @PostMapping
    public UserTokenDTO signIn(@RequestBody UserLoginDTO userLoginDTO) {
        return keycloakUserService.authenticate(userLoginDTO);
    }
}
