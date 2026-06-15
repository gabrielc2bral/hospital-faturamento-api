package br.com.monolithlabs.clinica.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDtoRequest {
    @Email(message = "Email Invalido!")
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(min = 8, max = 255)
    private String senha;

}