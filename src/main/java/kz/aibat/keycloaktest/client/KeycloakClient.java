package kz.aibat.keycloaktest.client;

import kz.aibat.keycloaktest.dto.UserCreateDTO;
import kz.aibat.keycloaktest.dto.UserLoginDTO;
import kz.aibat.keycloaktest.dto.UserTokenDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakClient {

    private final Keycloak keycloak;
    private final RestTemplate restTemplate;

    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String GRANT_TYPE = "grant_type";
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    public static final String LOGIN_EVENT = "LOGIN";
    public static final String REFRESH_TOKEN_EVENT = "REFRESH_TOKEN";
    public static final String LOGIN_ERROR_EVENT = "LOGIN_ERROR";
    public static final String UPDATE_PROFILE_EVENT = "UPDATE_PROFILE";

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    public UserRepresentation createUser(UserCreateDTO userCreateDTO) {
        UserRepresentation newUser = getUserRepresentation(userCreateDTO);

        Response response = keycloak
                .realm(realm)
                .users()
                .create(newUser);

        if(response.getStatus() != HttpStatus.CREATED.value()) {
            log.error("Error creating user {}, status {} ", userCreateDTO, response.getStatus());
            throw new RuntimeException("Failed create user in keycloak " + response.getStatus());
        }

        List<UserRepresentation> userRepresentationList = keycloak
                .realm(realm)
                .users()
                .search(userCreateDTO.getUsername());
        
        return userRepresentationList.get(0);
    }

    public UserTokenDTO signIn(UserLoginDTO userLoginDTO) {
        String tokenEndpoint = serverUrl + "realms/" + realm + "/protocol/openid-connect/token";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(GRANT_TYPE, "password");
        formData.add(CLIENT_ID, clientId);
        formData.add(CLIENT_SECRET, clientSecret);
        formData.add(USERNAME, userLoginDTO.getUsername());
        formData.add(PASSWORD, userLoginDTO.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, new HttpEntity<>(formData, headers), Map.class);

        Map<String, Object> responseBody = response.getBody();

        if(!response.getStatusCode().is2xxSuccessful() || responseBody == null) {
            log.error("Error sign in in user {}", userLoginDTO);
            throw new RuntimeException("Error on sign in in with user" + response.getStatusCode());
        }

        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setAccessToken((String) responseBody.get("access_token"));
        userTokenDTO.setRefreshToken((String) responseBody.get("refresh_token"));
        return userTokenDTO;
    }

    private static UserRepresentation getUserRepresentation(UserCreateDTO userCreateDTO) {
        UserRepresentation newUser = new UserRepresentation();
        newUser.setEmail(userCreateDTO.getEmail());
        newUser.setEmailVerified(true);
        newUser.setUsername(userCreateDTO.getUsername());
        newUser.setFirstName(userCreateDTO.getFirstName());
        newUser.setLastName(userCreateDTO.getLastName());
        newUser.setEnabled(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(userCreateDTO.getPassword());
        credentialRepresentation.setTemporary(false);

        newUser.setCredentials(List.of(credentialRepresentation));
        return newUser;
    }
}
