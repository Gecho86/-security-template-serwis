package com.j24.security.template.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TodoTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String content;

    @CreationTimestamp
    private LocalDateTime timeAdded;

    private LocalDateTime timeFinished;

    @Enumerated(value = EnumType.STRING)
    private TodoTaskStatus todoTaskStatus;

    @ManyToOne()
    private Account user;
}
