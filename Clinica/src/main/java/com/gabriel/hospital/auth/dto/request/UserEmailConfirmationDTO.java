package com.gabriel.hospital.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEmailConfirmationDTO {
    @Email(message = "Email Invalido!")
    @Size(max = 150)
    private String email;
    private String code;
}
