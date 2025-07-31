package com.serviconli.task.dto;

import com.serviconli.task.model.EstadoTarea;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TareaUpdateDTO {
    private Long id;
    private EstadoTarea estado;
    private String observaciones;
    private LocalDateTime fechaRecordatorio;

    private String numeroAutorizacion;
    private String numeroRadicado;
    private String fechaCita;
    private String horaCita;
    private String doctor;
    private String ubicacion;
    private String especificaciones;
}
