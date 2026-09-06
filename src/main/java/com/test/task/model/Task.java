package com.test.task.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "tasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String descripcion;

    @Column(nullable = false)
    private OffsetDateTime fechaCreacion;

    private OffsetDateTime fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskState estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = OffsetDateTime.now(ZoneOffset.UTC);
        if (this.estado == null) {
            this.estado = TaskState.PENDIENTE;
        }
    }

}