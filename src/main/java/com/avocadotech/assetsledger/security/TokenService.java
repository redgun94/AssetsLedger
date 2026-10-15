package com.avocadotech.assetsledger.security;


import com.avocadotech.assetsledger.auth.dto.AuthResponseDto;
import lombok.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private  final Duration expiration;

    public TokenService(JwtEncoder jwtEncoder, @Value("${app.jwt.expiration-minutes}") long expirationMinutes ){
        this.jwtEncoder = jwtEncoder;
        this.expiration = Duration.ofMinutes(expirationMinutes);
    }
    public AuthResponseDto issue(AuthenticatedUser user){
        Instant now = Instant.now();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("assetsledger")
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plus(expiration))
                .claim("role", user.getRole())
                .build();

        String token = jwtEncoder
                .encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();

        return new AuthResponseDto(token, "Bearer", expiration.toSeconds());
    }

}
