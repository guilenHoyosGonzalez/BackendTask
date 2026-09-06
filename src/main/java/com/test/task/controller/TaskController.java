package com.test.task.controller;

import com.test.task.model.*;
import com.test.task.repository.UserRepository;
import com.test.task.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private static final Logger logger=
            LoggerFactory.getLogger(TaskController.class);


    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    // Crear tarea
    @PostMapping("/createTask")
    public ResponseEntity<Map<String, String>> createTask(
            @RequestBody Map<String, Object> request) {

        Map<String, String> answer = new HashMap<>();

        // Obtener los valores enviados en el JSON
        String titulo = (String) request.get("titulo");
        String descripcion = (String) request.get("descripcion");
        String estado = (String) request.get("estado");
        String userEmail = (String) request.get("email");
        String fechaCreacion = (String) request.get("fechaCreacion");
        String fechaVencimiento = (String) request.get("fechaVencimiento");


        if (titulo == null || titulo.trim().isEmpty()) {
            answer.put("error", "El título no puede estar vacío");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(answer);
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            answer.put("error", "La descripción no puede estar vacía");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(answer);
        }

        if (userEmail == null || userEmail.trim().isEmpty()) {
            answer.put("error", "El email no puede estar vacío");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(answer);
        }

        if (estado == null || estado.trim().isEmpty()) {
            answer.put("error", "El estado no puede estar vacío");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(answer);
        }
        if (fechaCreacion == null || fechaCreacion.trim().isEmpty()) {
            answer.put("error", "El fecha creacion no puede estar vacía");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(answer);
        }

        if (fechaVencimiento == null || fechaVencimiento.trim().isEmpty()) {
            answer.put("error", "El fecha vencimiento no puede estar vacía");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(answer);
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o sin permisos"));

        Task task = new Task();
        task.setDescripcion(descripcion);
        task.setTitulo(titulo);
        task.setEstado(TaskState.valueOf(estado));
        task.setFechaCreacion(OffsetDateTime.parse(fechaCreacion));
        task.setFechaVencimiento(OffsetDateTime.parse(fechaVencimiento));
        task.setUser(user);

        if (task != null) {
           this.taskService.createTask(task);
            answer.put("success", "Tarea actualizada correctamente");
            answer.put("id", String.valueOf(task.getId()));
            answer.put("titulo", task.getTitulo());
            answer.put("descripcion", task.getDescripcion());
            answer.put("fechaCreacion", String.valueOf(task.getFechaCreacion()));
            answer.put("fechaVencimiento", String.valueOf(task.getFechaVencimiento()));
            answer.put("user nombre", String.valueOf(task.getUser().getId()));


            logger.info("que erees getTitulo: "+task.getTitulo());
            logger.info("que erees getDescripcion: "+task.getDescripcion());
            logger.info("que erees getEstado: "+task.getEstado());
            logger.info("que erees getFechaCreacion: "+task.getFechaCreacion());
            logger.info("que erees getFechaVencimiento: "+task.getFechaVencimiento());

            return ResponseEntity.status(HttpStatus.OK).body(answer);
        } else {
            answer.put("error", "No se pudo registar la tarea ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(answer);
        }

    }

    // Listar tareas del usuario
    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> getTasks(Pageable pageable) {
        String userEmail = ((User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getEmail();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o sin permisos"));

        Page<TaskResponseDTO> tasks = taskService.getTasksByUser(user.getId(), pageable);
        return ResponseEntity.ok(tasks);
    }


    // Actualizar tarea
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateTask(
            @PathVariable Long id,
            @RequestBody DTOTask request) {

        Map<String, String> answer = new HashMap<>();
        Task task = this.taskService.getTaskById(id);

        if (task != null) {
            if (request.getTitulo() == null || request.getTitulo().trim().isEmpty()) {
                answer.put("error", "El título no puede estar vacío");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(answer);
            }

            if (request.getDescripcion() == null || request.getDescripcion().trim().isEmpty()) {
                answer.put("error", "La descripción no puede estar vacía");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(answer);
            }

            task.setDescripcion(request.getDescripcion());
            task.setTitulo(request.getTitulo());
            task.setEstado(
                    (request.getEstado() != null && !request.getEstado().isBlank())
                            ? TaskState.valueOf(request.getEstado())
                            : TaskState.PENDIENTE
            );
            task.setFechaCreacion(request.getFechaCreacion());
            task.setFechaVencimiento(request.getFechaVencimiento());

            this.taskService.updateTask(task);

            answer.put("success", "Tarea actualizada correctamente");
            answer.put("id", String.valueOf(task.getId()));
            answer.put("titulo", task.getTitulo());
            answer.put("descripcion", task.getDescripcion());
            answer.put("fechaCreacion", String.valueOf(task.getFechaCreacion()));
            answer.put("fechaVencimiento", String.valueOf(task.getFechaVencimiento()));
            answer.put("user nombre", task.getUser().getNombre());

            return ResponseEntity.status(HttpStatus.OK).body(answer);

        } else {
            answer.put("error", "No se encontró la tarea con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(answer);
        }
    }

    // Eliminar tarea
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable long id) {
        Map<String, String> answer = new HashMap<>();

        Task task = this.taskService.getTaskById(id);
        String userEmail = ((User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getEmail();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o sin permisos"));


        if (task == null) {
            answer.put("error", "No se encontró la tarea con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(answer);
        }
        this.taskService.deleteTask(id);
        answer.put("success", "Tarea eliminada correctamente");
        answer.put("id", String.valueOf(id));

        return ResponseEntity.ok(answer);
    }

}
