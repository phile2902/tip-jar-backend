package com.bitcoin.interview.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    private String name;
    private String email;
    private String address;
    private Boolean isAdmin = false;

    public User(String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }
    
    public User(String name, String email, String address, Boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.isAdmin = isAdmin;
    }
    
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Payment> payments;
}
