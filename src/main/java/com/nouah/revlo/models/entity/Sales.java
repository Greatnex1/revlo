package com.nouah.revlo.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder
@Table
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
//    @JoinColumn(name = "productId")
    private Set<Product> products;
    private String productId;
    @ManyToOne
    private Client client;
    @ManyToOne
    private AppUser seller;
    private BigDecimal totalAmount;
    private int quantity;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreated;
    @Column(insertable = false)
    private LocalDateTime dateLastModified;
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Long createdBy;
    @LastModifiedBy
    @Column(insertable = false)
    private Long lastModifiedBy;

}
