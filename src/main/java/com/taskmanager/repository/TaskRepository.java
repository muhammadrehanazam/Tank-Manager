package com.taskmanager.repository;
import com.taskmanager.model.Task;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long>{
    List<Task>findByUserId(Long userId);
    List<Task> findByUserIdAndStatus(Long userId, Task.Status status);
}
