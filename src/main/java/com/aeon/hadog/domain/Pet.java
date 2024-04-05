package com.aeon.hadog.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable=false)
    private String name;

    @Column
    private String breed;

    @Column(nullable=false)
    private Boolean sex; // 남: 1, 여: 0

    @Column(nullable=false)
    private Boolean neuter;

    @Column(length = 500)
    private String image;

    @Column
    private Integer age;
}
