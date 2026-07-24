package com.taskmanager.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.taskmanager.model.Task;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY )
    private Long id;
    private String name;

    @Column(unique = true,nullable = false)
    private String email;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true) // task has no meaning without user
    @com.fasterxml.jackson.annotation.JsonIgnore // <--- Add this!
    private List<Task> tasks;

}
