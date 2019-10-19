package com.j24.security.template.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private boolean locked;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Cascade(value = {org.hibernate.annotations.CascadeType.DETACH})
    private Set<AccountRole> roles;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "user")
    @Cascade(value = {org.hibernate.annotations.CascadeType.DELETE})
    private Set<TodoTask> tasks;

    public boolean isAdmin() {
        return roles.stream()
                .map(AccountRole::getName)
                .anyMatch(s -> s.equals("ADMIN"));
    }

}
