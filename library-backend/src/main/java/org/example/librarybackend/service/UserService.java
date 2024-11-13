package org.example.librarybackend.service;

import org.example.librarybackend.dto.UserAuthResponseDTO;
import org.example.librarybackend.dto.UserLoginDTO;
import org.example.librarybackend.dto.UserRegisterDTO;
import org.example.librarybackend.model.User;
import org.example.librarybackend.model.Role;
import org.example.librarybackend.repository.RoleRepository;
import org.example.librarybackend.repository.UserRepository;
import org.example.librarybackend.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    public UserAuthResponseDTO login(UserLoginDTO loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        var jwtToken = jwtService.generateToken(loginDto.getEmail(), roleNames);


        return new UserAuthResponseDTO(jwtToken);
    }

    public UserAuthResponseDTO register(UserRegisterDTO registerDto) {
        Optional<User> u = userRepository.findByEmail(registerDto.getEmail());

        if (u.isPresent()){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setAge(registerDto.getAge());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        user.setRoles(roles);

        userRepository.save(user);

        Set<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        var jwtToken = jwtService.generateToken(user.getEmail(), roleNames);

        return new UserAuthResponseDTO(jwtToken);
    }


}
