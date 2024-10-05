package ru.mtuci.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="login")
    private String login;

    @Column(name="password")
    private String password;

    @Column(name="role")
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<License> licenses;

}
