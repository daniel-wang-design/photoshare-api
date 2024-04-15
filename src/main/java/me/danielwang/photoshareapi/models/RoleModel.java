package me.danielwang.photoshareapi.models;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class RoleModel {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long roleId;

    @Column(length = 60)
    private String name;
}
