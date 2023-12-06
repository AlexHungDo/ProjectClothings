package com.example.demo.Service;
import com.example.demo.Untity.Role;
import com.example.demo.Untity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import org.springframework.security.core.userdetails.UserDetails;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import com.example.demo.Reponsitory.UserRepository;
import com.example.demo.dto.UserRegistrationDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Override
    public User save(UserRegistrationDto registrationDto) {
        var user = User.builder()
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .roles(List.of(new Role("ROLE_USER")))
                .build();
        return userRepository.save(user);
    }

    @Override
    public User admin() {
        var admin = User.builder()
                .firstName("Xavier")
                .lastName("Do")
                .email("hungdoviet456@gmail.com")
                .password(passwordEncoder.encode("123"))
                .roles(List.of(new Role("ROLE_ADMIN")))
                .build();
        return userRepository.save(admin);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException
                    ("Invalid username or password.");
        }
        return new org.springframework.security
                .core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority>
    mapRolesToAuthorities(Collection<Role> roles) {

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority
                        (role.getName()))
                .collect(Collectors.toList());
    }
}
