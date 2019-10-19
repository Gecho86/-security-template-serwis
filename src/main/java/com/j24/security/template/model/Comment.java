package com.j24.security.template.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Long id;
    private String name;
    private String description;

}
