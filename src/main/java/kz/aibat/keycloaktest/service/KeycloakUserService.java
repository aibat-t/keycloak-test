package kz.aibat.keycloaktest.service;

import kz.aibat.keycloaktest.client.KeycloakClient;
import kz.aibat.keycloaktest.dto.UserCreateDTO;
import kz.aibat.keycloaktest.dto.UserDTO;
import kz.aibat.keycloaktest.dto.UserLoginDTO;
import kz.aibat.keycloaktest.dto.UserTokenDTO;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private final KeycloakClient keycloakClient;

    public UserDTO registerUser(UserCreateDTO userCreateDTO) {
        UserRepresentation userRepresentation = keycloakClient.createUser(userCreateDTO);

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(userRepresentation.getEmail());
        userDTO.setUsername(userRepresentation.getUsername());
        userDTO.setLastName(userRepresentation.getLastName());
        userDTO.setFirstName(userRepresentation.getFirstName());

        return userDTO;
    }

    public UserTokenDTO authenticate(UserLoginDTO userLoginDTO) {
        return keycloakClient.signIn(userLoginDTO);
    }
}
