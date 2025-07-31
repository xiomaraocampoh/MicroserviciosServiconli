package com.serviconli.task.service.impl;


import com.serviconli.task.dto.HistorialTareaResponseDTO;
import com.serviconli.task.dto.TareaRequestDTO;
import com.serviconli.task.dto.TareaResponseDTO;
import com.serviconli.task.dto.TareaUpdateDTO;
import com.serviconli.task.exception.ResourceNotFoundException;
import com.serviconli.task.exception.InvalidTaskStateException;
import com.serviconli.task.model.*;
import com.serviconli.task.repository.HistorialTareaRepository;
import com.serviconli.task.repository.TareaRepository;
import com.serviconli.task.service.TareaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TareaServiceImpl implements TareaService {

    private final TareaRepository tareaRepository;
    private final HistorialTareaRepository historialTareaRepository;

    @Autowired
    public TareaServiceImpl(TareaRepository tareaRepository, HistorialTareaRepository historialTareaRepository) {
        this.tareaRepository = tareaRepository;
        this.historialTareaRepository = historialTareaRepository;
    }

    @Override
    @Transactional
    public TareaResponseDTO crearTarea(TareaRequestDTO dto) {
        Tarea tarea = new Tarea();
        BeanUtils.copyProperties(dto, tarea);

        tarea.setEstado(EstadoTarea.PENDIENTE);
        tarea.setFechaCreacion(LocalDateTime.now());
        tarea.setFechaActualizacion(LocalDateTime.now());

        if (dto.getFecha() != null && dto.getHora() != null) {
            try {
                LocalDateTime fechaCita = LocalDateTime.parse(dto.getFecha() + "T" + dto.getHora());
                tarea.setFechaRecordatorio(fechaCita.minusDays(1));
            } catch (Exception e) {
                tarea.setFechaRecordatorio(null);
            }
        }

        Tarea saved = tareaRepository.save(tarea);

        HistorialTarea historial = new HistorialTarea();
        historial.setTarea(saved);
        historial.setEstadoAnterior(null);
        historial.setEstadoNuevo(EstadoTarea.PENDIENTE);
        historial.setDescripcionCambio("Tarea creada");
        historial.setUsuarioCambio("System");
        historial.setFechaCambio(LocalDateTime.now());

        historialTareaRepository.save(historial);

        return convertToDto(saved);
    }

    @Override
    public TareaResponseDTO obtenerTareaPorId(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con ID: " + id));
        return convertToDto(tarea);
    }

    @Override
    public List<TareaResponseDTO> obtenerTodasLasTareas() {
        return tareaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TareaResponseDTO actualizarTarea(Long id, TareaUpdateDTO dto) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con ID: " + id));

        if (dto.getEstado() != null && !dto.getEstado().equals(tarea.getEstado())) {
            EstadoTarea anterior = tarea.getEstado();
            tarea.setEstado(dto.getEstado());
            registrarHistorialCambioEstado(tarea, anterior, dto.getEstado(), "Cambio manual de estado");
        }

        if (dto.getObservaciones() != null) tarea.setObservaciones(dto.getObservaciones());
        if (dto.getFechaRecordatorio() != null) tarea.setFechaRecordatorio(dto.getFechaRecordatorio());
        if (dto.getDoctor() != null) tarea.setDoctor(dto.getDoctor());
        if (dto.getUbicacion() != null) tarea.setUbicacion(dto.getUbicacion());
        if (dto.getFechaCita() != null) tarea.setFechaCita(dto.getFechaCita());
        if (dto.getHoraCita() != null) tarea.setHoraCita(dto.getHoraCita());
        if (dto.getNumeroAutorizacion() != null) tarea.setNumeroAutorizacion(dto.getNumeroAutorizacion());
        if (dto.getNumeroRadicado() != null) tarea.setNumeroRadicado(dto.getNumeroRadicado());
        if (dto.getEspecificaciones() != null) tarea.setEspecificaciones(dto.getEspecificaciones());

        tarea.setFechaActualizacion(LocalDateTime.now());

        return convertToDto(tareaRepository.save(tarea));
    }

    @Override
    @Transactional
    public void eliminarTarea(Long id) {
        if (!tareaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarea no encontrada con ID: " + id);
        }
        tareaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TareaResponseDTO cambiarEstadoTarea(Long id, EstadoTarea nuevoEstado) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con ID: " + id));

        EstadoTarea estadoAnterior = tarea.getEstado();

        if (!isValidTransition(estadoAnterior, nuevoEstado)) {
            throw new InvalidTaskStateException(String.format(
                    "Transición de estado inválida de '%s' a '%s' para la tarea con ID: %d",
                    estadoAnterior, nuevoEstado, id));
        }

        tarea.setEstado(nuevoEstado);
        tarea.setFechaActualizacion(LocalDateTime.now());
        Tarea updatedTarea = tareaRepository.save(tarea);

        registrarHistorialCambioEstado(updatedTarea, estadoAnterior, nuevoEstado, "Cambio de estado vía API");

        return convertToDto(updatedTarea);
    }

    @Override
    public List<TareaResponseDTO> filtrarTareas(EstadoTarea estado, Prioridad prioridad, String tipo, String paciente, String eps) {
        List<Tarea> tareas;
        if (estado != null && prioridad != null) {
            tareas = tareaRepository.findByEstadoAndPrioridad(estado, prioridad);
        } else if (estado != null) {
            tareas = tareaRepository.findByEstado(estado);
        } else if (prioridad != null) {
            tareas = tareaRepository.findByPrioridad(prioridad);
        } else if (tipo != null && !tipo.isEmpty()) {
            tareas = tareaRepository.findByTipo(tipo);
        } else if (paciente != null && !paciente.isEmpty()) {
            tareas = tareaRepository.findByPacienteContainingIgnoreCase((paciente));
        } else if (eps != null && !eps.isEmpty()) {
            tareas = tareaRepository.findByEpsContainingIgnoreCase(eps);
        } else {
            tareas = tareaRepository.findAll();
        }
        return tareas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistorialTareaResponseDTO> obtenerHistorialPorTarea(Long tareaId) {
        if (!tareaRepository.existsById(tareaId)) {
            throw new ResourceNotFoundException("Tarea no encontrada con ID: " + tareaId);
        }
        return historialTareaRepository.findByTareaIdOrderByFechaCambioAsc(tareaId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TareaResponseDTO convertToDto(Tarea tarea) {
        TareaResponseDTO dto = new TareaResponseDTO();
        BeanUtils.copyProperties(tarea, dto);
        return dto;
    }

    private HistorialTareaResponseDTO convertToDto(HistorialTarea historial) {
        HistorialTareaResponseDTO dto = new HistorialTareaResponseDTO();
        BeanUtils.copyProperties(historial, dto);
        dto.setTareaId(historial.getTarea().getId());
        return dto;
    }

    private void registrarHistorialCambioEstado(Tarea tarea, EstadoTarea estadoAnterior, EstadoTarea estadoNuevo, String descripcion) {
        HistorialTarea historial = new HistorialTarea();
        historial.setTarea(tarea);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        historial.setDescripcionCambio(descripcion);
        historial.setUsuarioCambio("System/Admin");
        historial.setFechaCambio(LocalDateTime.now());
        historialTareaRepository.save(historial);
    }

    private boolean isValidTransition(EstadoTarea estadoActual, EstadoTarea nuevoEstado) {
        if (estadoActual == nuevoEstado) return true;
        switch (estadoActual) {
            case PENDIENTE:
                return nuevoEstado == EstadoTarea.EN_PROGRESO;
            case EN_PROGRESO:
                return nuevoEstado == EstadoTarea.CITA_CONFIRMADA || nuevoEstado == EstadoTarea.ENVIADA;
            case CITA_CONFIRMADA:
                return nuevoEstado == EstadoTarea.ENVIADA;
            case ENVIADA:
                return nuevoEstado == EstadoTarea.COMPLETADA;
            case COMPLETADA:
                return false;
            default:
                return false;
        }
    }
}
