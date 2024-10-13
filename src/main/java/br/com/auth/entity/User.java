package br.com.auth.entity;

import br.com.auth.enumerate.UserRoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String cpf;

    private String password;
    private String email;
    private LocalDateTime lastLoginDate;
    private LocalDateTime last2FADate;
    private String twoFACode;
    private LocalDateTime twoFACodeExpiration;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
}

