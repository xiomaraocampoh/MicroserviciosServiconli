package com.serviconli.patientservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "beneficiarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beneficiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_identificacion")
    private String tipoIdentificacion;

    @Column(name = "numero_identificacion")
    private String numeroIdentificacion;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    @Column(name = "celular")
    private String celular;

    @Column(name = "correo")
    private String correo;

    @Column(name = "fecha_expedicion")
    private String fechaExpedicion;

    @Column(name = "parentezco")
    private String parentezco;

    @Column(name = "eps")
    private String eps;

    // Relación Many-to-One con Cotizante
    @ManyToOne(fetch = FetchType.LAZY) // Carga perezosa para evitar cargar el cotizante si no es necesario
    @JoinColumn(name = "cotizante_id", nullable = false) // Columna en la tabla beneficiarios que almacena la FK al cotizante
    private Cotizante cotizante;
}