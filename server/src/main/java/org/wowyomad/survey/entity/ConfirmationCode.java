package org.wowyomad.survey.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.wowyomad.survey.validation.ValidEmail;

import java.time.LocalDateTime;

@Entity
public class ConfirmationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ValidEmail
    @NotBlank
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @NotBlank
    private String code;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConfirmationType type;

    @Column(nullable = false)
    private boolean confirmed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public ConfirmationType getType() {
        return type;
    }

    public void setType(ConfirmationType type) {
        this.type = type;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
