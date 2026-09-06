package com.test.task.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class DTOTask {

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    @NotNull(message = "La fecha creación es obligatoria")
    private OffsetDateTime fechaCreacion;

    @NotNull(message = "La fecha vencimiento es obligatoria")
    private OffsetDateTime fechaVencimiento;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
