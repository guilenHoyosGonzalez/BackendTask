package com.test.task.service;

import com.test.task.model.Task;
import com.test.task.model.TaskResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITaskService {
    Task createTask(Task request);
    Page<TaskResponseDTO> getTasksByUser(Long userId, Pageable pageable);
    Task updateTask(Task task);
    void deleteTask(Long id);
    Task getTaskById(Long id);
}
