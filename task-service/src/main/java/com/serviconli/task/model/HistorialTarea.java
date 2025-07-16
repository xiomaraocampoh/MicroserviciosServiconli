package com.serviconli.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_tareas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Muchas entradas de historial para una tarea
    @JoinColumn(name = "tarea_id", nullable = false)
    private Tarea tarea;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private EstadoTarea estadoAnterior;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private EstadoTarea estadoNuevo;

    @Column(nullable = false)
    private LocalDateTime fechaCambio;

    @Column(length = 255)
    private String usuarioCambio; // Podría ser el usuario que realizó el cambio (futura integración de seguridad)

    @Column(length = 500)
    private String descripcionCambio; // Breve descripción del cambio

    @PrePersist
    protected void onCreate() {
        fechaCambio = LocalDateTime.now();
        if (this.usuarioCambio == null || this.usuarioCambio.isEmpty()) {
            this.usuarioCambio = "Sistema"; // Valor por defecto si no se especifica
        }
    }
}