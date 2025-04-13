package com.fervanz.auth.security.context;

import com.fervanz.auth.client.models.dao.ClientDao;
import com.fervanz.auth.client.models.entities.Client;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ClientDao clientDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return clientDao.findByUsername(username)
                .map(Client::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("UserDetailsServiceImpl :: User not found"));
    }
}
