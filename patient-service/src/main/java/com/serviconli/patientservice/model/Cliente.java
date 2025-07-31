package com.serviconli.patientservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes_serviconli")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ESTADO_DEL_CLIENTE")
    private String estadoDelCliente;

    @Column(name = "TIPO_DE_CLIENTE")
    private String tipoDeCliente;

    @Column(name = "Tipo_de_Cotizante")
    private String tipoDeCotizante;

    @Column(name = "tipo_identificacion")
    private String tipoIdentificacion;

    @Column(name = "numero_identificacion", nullable = false, length = 50)
    private String numeroIdentificacion;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    @Column(name = "Sexo")
    private String sexo;

    @Column(name = "Fecha_de_nacimiento")
    private String fechaNacimiento;

    @Column(name = "AFILIADO_DIRECCION")
    private String afiliadoDireccion;

    @Column(name = "AFILIADO_CIUDAD")
    private String afiliadoCiudad;

    @Column(name = "AFILIADO_DEPARTAMENTO")
    private String afiliadoDepartamento;

    @Column(name = "celular")
    private String celular;

    @Column(name = "fecha_expedicion")
    private String fechaExpedicion;

    @Column(name = "AFILIADO_CORREO_ELECTRONICO")
    private String afiliadoCorreoElectronico;

    @Column(name = "PAGADOR")
    private String pagador;

    @Column(name = "TIPO_DE_DOCUMENTO")
    private String tipoDeDocumento;

    @Column(name = "DOCUMENTO")
    private String documento;

    @Column(name = "CLIENTE_DIRECCION")
    private String clienteDireccion;

    @Column(name = "CLIENTE_CIUDAD")
    private String clienteCiudad;

    @Column(name = "CLIENTE_DEPARTAMENTO")
    private String clienteDepartamento;

    @Column(name = "CLIENTE_TELEFONO")
    private String clienteTelefono;

    @Column(name = "CORREO_ELECTRONICO")
    private String correoElectronico;

    @Column(name = "NOVEDAD")
    private String novedad;

    @Column(name = "FECHA_NOVEDAD")
    private String fechaNovedad;

    @Column(name = "OPERADOR")
    private String operador;

    @Column(name = "USUARIO")
    private String usuario;

    @Column(name = "CLAVE")
    private String clave;

    @Column(name = "SALARIO")
    private String salario;

    @Column(name = "NombreARL")
    private String nombreARL;

    @Column(name = "Clase_de_Riesgo_ARL")
    private String claseDeRiesgoARL;

    @Column(name = "US_PORTAL")
    private String usPortal;

    @Column(name = "CONT_PORTAL")
    private String contPortal;

    @Column(name = "NombreCCF")
    private String nombreCCF;

    @Column(name = "US_PORTAL_1")
    private String usPortal1;

    @Column(name = "CONT_PORTAL_1")
    private String contPortal1;

    @Column(name = "eps")
    private String eps;

    @Column(name = "US_PORTAL_2")
    private String usPortal2;

    @Column(name = "CONT_PORTAL_2")
    private String contPortal2;

    @Column(name = "Nombre_AFP")
    private String nombreAFP;

    @Column(name = "US_PORTAL_3")
    private String usPortal3;

    @Column(name = "CONT_PORTAL_3")
    private String contPortal3;

    @Column(name = "PARAFISCALES")
    private String parafiscales;

    @Column(name = "OBSERVACIONES_AFILIACIÓN")
    private String observacionesAfiliacion;

    @Column(name = "PERIOCIDAD_DE_PAGO")
    private String periocidadDePago;

    @Column(name = "REG_CONTABLE")
    private String regContable;

    @Column(name = "N_ULTIMO_DOCUMENTO")
    private String nUltimoDocumento;

    @Column(name = "MES_DE_PAGO")
    private String mesDePago;

    @Column(name = "PAGOS_AL_DÍA")
    private String pagosAlDia;

    @Column(name = "OBSERVACIONES_PAGOS")
    private String observacionesPagos;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "correo")
    private String correo;

    @Column(name = "estado_cotizante")
    private String estadoCotizante;
}