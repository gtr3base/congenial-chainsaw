package com.gtr3base.AvByAnalog.entity;

import com.gtr3base.AvByAnalog.enums.UserRole;
import jakarta.persistence.Entity;
import  jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Car> cars;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    @NotBlank(message = "Username is required")
    @Size(min = 0, max = 50, message = "Username must be between 0 and 50 chars")
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password minimum length is 6 and max 255")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @NotNull(message = "Role is required")
    private UserRole role = UserRole.USER;

    @Column(name = "created_at")
    @PastOrPresent(message = "Creation date cannot be in the future")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Favorite> favorites;
}
