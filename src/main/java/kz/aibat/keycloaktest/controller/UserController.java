package kz.aibat.keycloaktest.controller;

import kz.aibat.keycloaktest.dto.UserCreateDTO;
import kz.aibat.keycloaktest.dto.UserDTO;
import kz.aibat.keycloaktest.service.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final KeycloakUserService keycloakUserService;

    @PostMapping(value = "/create")
    public UserDTO createUser(@RequestBody UserCreateDTO userCreateDTO) {
        return keycloakUserService.registerUser(userCreateDTO);
    }
}
