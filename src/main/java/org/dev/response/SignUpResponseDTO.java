package org.dev.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResponseDTO {
    private String accessToken;
    private String refreshToken;
}
