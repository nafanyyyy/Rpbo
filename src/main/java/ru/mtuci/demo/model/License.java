package ru.mtuci.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="licenses")
public class License {
    @Id
    @Column(name="key")
    private String key;

    @Column(name="date_activate")
    private Date date_activate;

    @Column(name="date_exp")
    private Date date_exp;

    @Column(name="block")
    private Boolean block;

    @Column(name="device_id")
    private String device_id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference("back")
    private User user;

}
