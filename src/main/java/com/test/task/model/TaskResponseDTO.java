package com.test.task.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaVencimiento;
    private String userNombre;

    public TaskResponseDTO(Task task) {
        this.id = task.getId();
        this.titulo = task.getTitulo();
        this.descripcion = task.getDescripcion();
        this.estado = task.getEstado().toString();
        this.fechaCreacion = task.getFechaCreacion().toLocalDateTime();
        this.fechaVencimiento = task.getFechaVencimiento().toLocalDateTime();
        this.userNombre = task.getUser().getNombre();
    }



}
