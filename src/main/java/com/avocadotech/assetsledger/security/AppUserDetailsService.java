package com.avocadotech.assetsledger.security;

import com.avocadotech.assetsledger.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        return userRepository.findByEmail(email.trim().toLowerCase()).map(AuthenticatedUser::new).orElseThrow(
                ()->new UsernameNotFoundException("Invalid credentials")
        );
    }
}
