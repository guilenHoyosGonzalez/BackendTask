package com.test.task.service;

import com.test.task.model.Task;
import com.test.task.model.TaskResponseDTO;
import com.test.task.model.User;
import com.test.task.repository.TaskRepository;
import com.test.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TaskService implements ITaskService{

    @Autowired private TaskRepository taskRepository;
    @Autowired private UserRepository userRepository;

    public Task createTask(Task request) {
        return taskRepository.save(request);
    }

    public Page<TaskResponseDTO> getTasksByUser(Long userId, Pageable pageable) {
        List<TaskResponseDTO> filtered = taskRepository.findAll().stream()
                .filter(task -> task.getUser().getId().equals(userId))
                .map(TaskResponseDTO::new)
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<TaskResponseDTO> subList = start > filtered.size() ? List.of() : filtered.subList(start, end);

        return new PageImpl<>(subList, pageable, filtered.size());
    }



    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        taskRepository.delete(task);
    }

    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        return task;
    }
}
