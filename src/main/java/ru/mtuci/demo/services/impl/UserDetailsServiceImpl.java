package ru.mtuci.demo.services.impl;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.model.UserDetailsImpl;
import ru.mtuci.demo.repo.UserRepository;

@RequiredArgsConstructor
@Service
public final class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserDetailsImpl.fromUser(userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found")));
    }

}