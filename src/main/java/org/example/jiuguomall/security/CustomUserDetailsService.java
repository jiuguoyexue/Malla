package org.example.jiuguomall.security;

import org.example.jiuguomall.entity.User;
import org.example.jiuguomall.mapper.UserMapper;
import org.example.jiuguomall.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        List<String> roleCodes =
                userRoleMapper.selectRoleCodesByUserId(user.getId());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                roleCodes.stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toList())
        );
    }
}
