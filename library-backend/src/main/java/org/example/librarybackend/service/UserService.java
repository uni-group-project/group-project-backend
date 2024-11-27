package org.example.librarybackend.service;

import org.example.librarybackend.dto.*;
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

import java.util.*;
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

        Date d = user.getCardExpiryDate();

        if(user.getBlocked() || d != null && d.before(new Date())){
            return null;
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Set<String> roleNames = userRepository.findRolesByUserId(user.getId());
        var jwtToken = jwtService.generateToken(loginDto.getEmail(), user.getId(), roleNames);

        return new UserAuthResponseDTO(jwtToken);
    }

    public UserAuthResponseDTO registerReader(ReaderRegisterDTO registerDto) {
        Optional<User> u = userRepository.findByEmail(registerDto.getEmail());

        if (u.isPresent()){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setPhone(registerDto.getPhone());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setAge(registerDto.getAge());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        String cardNumber = generateCardNumber();
        user.setCardNumber(cardNumber);

        Date cardExpiryDate = generateExpiryDate();
        user.setCardExpiryDate(cardExpiryDate);

        Role adminRole = roleRepository.findByName("READER")
                .orElseThrow(() -> new RuntimeException("Role READER not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        user.setRoles(roles);

        userRepository.save(user);

        Set<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        var jwtToken = jwtService.generateToken(user.getEmail(), user.getId(), roleNames);

        return new UserAuthResponseDTO(jwtToken);
    }


    public LibrarianAuthResponseDTO registerLibrarian(LibrarianRegisterDTO registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Email is already taken.");
        }

        User librarian = new User();
        librarian.setFirstName(registerDto.getFirstName());
        librarian.setLastName(registerDto.getLastName());
        librarian.setEmail(registerDto.getEmail());
        String password = generatePassword();

        librarian.setPassword(passwordEncoder.encode(password));
        librarian.setBlocked(false);

        Role librarianRole = roleRepository.findByName("LIBRARIAN")
                .orElseThrow(() -> new RuntimeException("Role LIBRARIAN not found"));
        librarian.setRoles(Set.of(librarianRole));

        userRepository.save(librarian);

        return new LibrarianAuthResponseDTO(librarian.getEmail(), password);
    }

    public UserDTO getReaderByCardNumber(String cardNumber) {
        User u = userRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Reader with card number not found"));

        return mapToUserDTO(u);
    }

    public UserDTO getUserById(Long userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return mapToUserDTO(u);
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public void blockReader(Long readerId) {
        User user = userRepository.findById(readerId)
                .orElseThrow(() -> new RuntimeException("Reader not found"));

        Set<String> roles = userRepository.findRolesByUserId(user.getId());

        boolean isReader = roles.stream()
                .anyMatch(role -> role.equalsIgnoreCase("READER"));

        if (!isReader) {
            throw new RuntimeException("User is not a reader");
        }

        user.setBlocked(true);
        userRepository.save(user);
    }

    private String generateCardNumber() {
        Random random = new Random();
        String cardNumber;
        boolean exists;

        do {
            cardNumber = String.valueOf(10000000 + random.nextInt(90000000));
            exists = userRepository.existsByCardNumber(cardNumber);
        } while (exists);

        return cardNumber;
    }

    private Date generateExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 1);
        return calendar.getTime();
    }

    private UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setAge(user.getAge());
        userDTO.setCardNumber(user.getCardNumber());
        userDTO.setCardExpiryDate(user.getCardExpiryDate());
        userDTO.setBlocked(user.getBlocked());
        userDTO.setRoles(userRepository.findRolesByUserId(user.getId()));
        return userDTO;
    }
}
