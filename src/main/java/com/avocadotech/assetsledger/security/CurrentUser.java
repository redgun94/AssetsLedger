package com.avocadotech.assetsledger.security;

import com.avocadotech.assetsledger.common.exception.InvalidSessionException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public final class CurrentUser {

    private CurrentUser(){}

    public static UUID toId(Jwt jwt){
        String subjectId = jwt.getSubject();
        if( subjectId == null){
            throw new InvalidSessionException();
        }
        try {
            return UUID.fromString(subjectId);
        } catch (Exception e) {
            throw new InvalidSessionException();
        }
    }
}
