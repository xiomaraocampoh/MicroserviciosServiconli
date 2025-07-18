package com.serviconli.task.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tareas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tipo de tarea no puede estar vacío")
    @Size(max = 50, message = "El tipo de tarea no puede exceder 50 caracteres")
    @Column(nullable = false, length = 50)
    private String tipo; // Ejemplo: "cita", "llamada", "reprogramación"

    @NotBlank(message = "El nombre del paciente no puede estar vacío")
    @Size(max = 255, message = "El nombre del paciente no puede exceder 255 caracteres")
    @Column(nullable = false)
    private String paciente;

    @NotBlank(message = "La EPS no puede estar vacía")
    @Size(max = 100, message = "La EPS no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String eps;

    @NotNull(message = "La prioridad no puede ser nula")
    @Enumerated(EnumType.STRING) // Almacena el Enum como String en la BD
    @Column(nullable = false, length = 20)
    private Prioridad prioridad;

    @NotNull(message = "El estado no puede ser nulo")
    @Enumerated(EnumType.STRING) // Almacena el Enum como String en la BD
    @Column(nullable = false, length = 30)
    private EstadoTarea estado;

    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    @Column(length = 1000)
    private String observaciones;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    private LocalDateTime fechaRecordatorio; // Opcional, para recordatorios futuros


    @Column(length = 100)
    private String telefono;

    @Column(length = 100)
    private String doctor;

    @Column(length = 100)
    private String ubicacion;

    @Column(length = 20)
    private String fecha; // Recomendado: usar LocalDate si es solo fecha de la cita

    @Column(length = 10)
    private String hora; // Recomendado: usar LocalTime si es solo hora de la cita



    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}