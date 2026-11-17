package com.avocadotech.assetsledger.security;

import com.avocadotech.assetsledger.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AuthenticatedUser implements UserDetails {
    @Getter
    private final UUID id;
    private final String email;
    private final String passwordHash;
    @Getter
    private final User.Role role;
    private final boolean enabled;

    public AuthenticatedUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.passwordHash = user.getPassword();
        this.role = user.getRole();
        this.enabled = !((user.getAccountStatus()  == User.Account_Status.SUSPENDED ) || (user.getAccountStatus() == User.Account_Status.DELETED));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }
    @Override public String getUsername() { return email; }
    @Override public String getPassword() { return passwordHash; }
    @Override public boolean isEnabled() { return enabled; }
}
