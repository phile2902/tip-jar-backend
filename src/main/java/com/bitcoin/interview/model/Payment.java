package com.bitcoin.interview.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import javax.transaction.Transactional;

@Data
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Min(value = 0L, message = "The value of amount must be positive")
    @NotNull(message = "Amount can not be null")
    private Double amount;

    @Min(value = 0L, message = "The value of tip must be positive")
    @NotNull(message = "Tip can not be null")
    private Double tip;

    private String thumbnail;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Payment(Double amount, Double tip, String thumbnail, User user) {
        this.amount = amount;
        this.tip = tip;
        this.thumbnail = thumbnail;
        this.user = user;
    }

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
