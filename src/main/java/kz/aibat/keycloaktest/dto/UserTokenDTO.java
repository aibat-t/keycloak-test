package kz.aibat.keycloaktest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenDTO {
    private String accessToken;
    private String refreshToken;
}
