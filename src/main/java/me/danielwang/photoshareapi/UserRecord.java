package me.danielwang.photoshareapi;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRecord {
    @Id
    @GeneratedValue(strategy = AUTO)
    private long userId;

    @Column(name = "name")
    private String name;

    public UserRecord(String name) {
        this.name = name;
    }
}
