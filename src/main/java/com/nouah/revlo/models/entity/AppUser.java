package com.nouah.revlo.models.entity;

import com.nouah.revlo.models.enums.Authority;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Builder
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable=false)
    private String username;
    private String password;
    private String phoneNumber;
    @CreationTimestamp
    private String dateCreated;
    @Enumerated(value = EnumType.STRING)
    private Authority authority;
 }
