package org.example.librarybackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Firstname must not be blank")
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Lastname must not be blank")
    private String lastName;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Phone number must be valid and contain 10 to 15 digits, optionally starting with '+'")
    @Column(unique = true)
    private String phone;

    @Column(nullable = false)
    @NotBlank(message = "Password must not be blank")
    private String password;

    private Integer age;

    private String cardNumber;

    private Date cardExpiryDate;

    @Column(nullable = false)
    private Boolean blocked = false;


    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LibrarianNote> librarianNotes = new ArrayList<>();
}
