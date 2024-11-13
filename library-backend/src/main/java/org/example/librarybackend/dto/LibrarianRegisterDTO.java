package org.example.librarybackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LibrarianRegisterDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
}