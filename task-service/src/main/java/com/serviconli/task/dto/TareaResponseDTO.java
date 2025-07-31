package com.serviconli.task.dto;


import com.serviconli.task.model.TipoPaciente;
import com.serviconli.task.model.TipoCita;
import com.serviconli.task.model.Prioridad;
import com.serviconli.task.model.EstadoTarea;
import lombok.Data;

@Data
public class TareaResponseDTO {
    private Long id;

    private TipoPaciente tipoPaciente;
    private String Paciente;
    private String tipoIdentificacionPaciente;
    private String numeroIdentificacionPaciente;
    private String celularPaciente;
    private String parentesco;

    private TipoCita tipoCita;
    private Prioridad prioridad;
    private String fecha;
    private String hora;
    private String doctor;
    private String eps;
    private String ubicacion;
    private String especificaciones;
    private String observaciones;
    private String numeroAutorizacion;
    private String numeroRadicado;
    private String fechaCita;
    private String horaCita;
    private String tipo;

    private EstadoTarea estado;
    private String fechaCreacion;
    private String fechaActualizacion;
}
