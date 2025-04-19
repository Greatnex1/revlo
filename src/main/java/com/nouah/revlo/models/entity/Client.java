package com.nouah.revlo.models.entity;

import com.nouah.revlo.models.enums.Authority;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String phoneNumber;
    @Email
    private String email;
    private String address;
    @Enumerated(value = EnumType.STRING)
    private Authority authority;
    @Column(nullable = false)
    private BigDecimal totalSpent;
    private LocalDateTime lastPurchaseDate;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreated;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime dateLastModified;
    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;
    @LastModifiedBy
    @Column(insertable = false)
    private Long lastModifiedBy;

}
