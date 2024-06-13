package org.wowyomad.survey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.wowyomad.survey.validation.ValidEmail;

public class UserLoginDto {
    @ValidEmail
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
