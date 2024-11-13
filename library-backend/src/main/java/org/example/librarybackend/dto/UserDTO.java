package org.example.librarybackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Integer age;

    private String cardNumber;

    private Date cardExpiryDate;

    private Boolean blocked;

    private Set<String> roles;
}
