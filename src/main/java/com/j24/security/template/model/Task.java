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
public class Task {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private String description;
        private String status;

        @Enumerated(value = EnumType.STRING)
        private String acceptationStatus;

        @CreationTimestamp
        private LocalDateTime timeAdded;
        private LocalDateTime timeTaskStarted;
        private LocalDateTime timeTaskFinished;

        @Enumerated(value = EnumType.STRING)
        private TodoTaskStatus todoTaskStatus;

        @ManyToOne()
        private Account user;

    }

}
