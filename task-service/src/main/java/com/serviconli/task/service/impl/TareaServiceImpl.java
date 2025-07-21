package com.serviconli.task.service.impl;

import com.serviconli.task.dto.HistorialTareaResponseDTO;
import com.serviconli.task.dto.TareaRequestDTO;
import com.serviconli.task.dto.TareaResponseDTO;
import com.serviconli.task.dto.TareaUpdateDTO;
import com.serviconli.task.exception.ResourceNotFoundException;
import com.serviconli.task.exception.InvalidTaskStateException;
import com.serviconli.task.model.EstadoTarea;
import com.serviconli.task.model.HistorialTarea;
import com.serviconli.task.model.Prioridad;
import com.serviconli.task.model.Tarea;
import com.serviconli.task.repository.HistorialTareaRepository;
import com.serviconli.task.repository.TareaRepository;
import com.serviconli.task.service.TareaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    public TareaResponseDTO crearTarea(TareaRequestDTO tareaRequestDTO) {
        Tarea tarea = new Tarea();
        BeanUtils.copyProperties(tareaRequestDTO, tarea);

        tarea.setEstado(EstadoTarea.PENDIENTE); // Estado inicial
        tarea.setFechaCreacion(LocalDateTime.now());
        tarea.setFechaActualizacion(LocalDateTime.now());

        // Nuevos campos
        tarea.setTelefono(tareaRequestDTO.getTelefono());
        tarea.setDoctor(tareaRequestDTO.getDoctor());
        tarea.setUbicacion(tareaRequestDTO.getUbicacion());
        tarea.setFecha(tareaRequestDTO.getFecha());
        tarea.setHora(tareaRequestDTO.getHora());

        Tarea savedTarea = tareaRepository.save(tarea);

        // Historial inicial
        HistorialTarea historial = new HistorialTarea();
        historial.setTarea(savedTarea);
        historial.setEstadoAnterior(null);
        historial.setEstadoNuevo(EstadoTarea.PENDIENTE);
        historial.setDescripcionCambio("Tarea creada con estado PENDIENTE");
        historialTareaRepository.save(historial);

        return convertToDto(savedTarea);
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
    public TareaResponseDTO actualizarTarea(Long id, TareaUpdateDTO tareaUpdateDTO) {
        Tarea tareaExistente = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con ID: " + id));

        // Campos existentes
        if (tareaUpdateDTO.getTipo() != null) tareaExistente.setTipo(tareaUpdateDTO.getTipo());
        if (tareaUpdateDTO.getPaciente() != null) tareaExistente.setPaciente(tareaUpdateDTO.getPaciente());
        if (tareaUpdateDTO.getEps() != null) tareaExistente.setEps(tareaUpdateDTO.getEps());
        if (tareaUpdateDTO.getPrioridad() != null) tareaExistente.setPrioridad(tareaUpdateDTO.getPrioridad());
        if (tareaUpdateDTO.getObservaciones() != null) tareaExistente.setObservaciones(tareaUpdateDTO.getObservaciones());
        if (tareaUpdateDTO.getFechaRecordatorio() != null) tareaExistente.setFechaRecordatorio(tareaUpdateDTO.getFechaRecordatorio());

        // Nuevos campos
        if (tareaUpdateDTO.getTelefono() != null) tareaExistente.setTelefono(tareaUpdateDTO.getTelefono());
        if (tareaUpdateDTO.getDoctor() != null) tareaExistente.setDoctor(tareaUpdateDTO.getDoctor());
        if (tareaUpdateDTO.getUbicacion() != null) tareaExistente.setUbicacion(tareaUpdateDTO.getUbicacion());
        if (tareaUpdateDTO.getFecha() != null) tareaExistente.setFecha(tareaUpdateDTO.getFecha());
        if (tareaUpdateDTO.getHora() != null) tareaExistente.setHora(tareaUpdateDTO.getHora());

        // Cambiar estado si viene en la petición
        if (tareaUpdateDTO.getEstado() != null && !tareaUpdateDTO.getEstado().equals(tareaExistente.getEstado())) {
            EstadoTarea estadoAnterior = tareaExistente.getEstado();
            tareaExistente.setEstado(tareaUpdateDTO.getEstado());
            registrarHistorialCambioEstado(tareaExistente, estadoAnterior, tareaUpdateDTO.getEstado(), "Actualización directa de estado");
        }

        tareaExistente.setFechaActualizacion(LocalDateTime.now());

        Tarea updatedTarea = tareaRepository.save(tareaExistente);
        return convertToDto(updatedTarea);
    }


    @Override
    public void eliminarTarea(Long id) {
        if (!tareaRepository.existsById(id)) {
            throw new EntityNotFoundException("La tarea con ID " + id + " no existe");
        }
        tareaRepository.deleteById(id);
    }


    @Override
    @Transactional
    public TareaResponseDTO cambiarEstadoTarea(Long id, EstadoTarea nuevoEstado) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con ID: " + id));

        EstadoTarea estadoAnterior = tarea.getEstado();

        // Validar transiciones de estado
        if (!isValidTransition(estadoAnterior, nuevoEstado)) {
            throw new InvalidTaskStateException(String.format(
                    "Transición de estado inválida de '%s' a '%s' para la tarea con ID: %d",
                    estadoAnterior, nuevoEstado, id));
        }

        tarea.setEstado(nuevoEstado);
        tarea.setFechaActualizacion(LocalDateTime.now());
        Tarea updatedTarea = tareaRepository.save(tarea);

        // Registrar en el historial
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
            tareas = tareaRepository.findByPacienteContainingIgnoreCase(paciente);
        } else if (eps != null && !eps.isEmpty()) {
            tareas = tareaRepository.findByEpsContainingIgnoreCase(eps);
        }
        else {
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

    // --- Métodos de Ayuda ---

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
        // Si tuviéramos un contexto de seguridad, podríamos obtener el usuario actual
        historial.setUsuarioCambio("System/Admin"); // Placeholder
        historialTareaRepository.save(historial);
    }

    /**
     * Define las transiciones de estado válidas para el sistema Kanban.
     */
    private boolean isValidTransition(EstadoTarea estadoActual, EstadoTarea nuevoEstado) {
        if (estadoActual == nuevoEstado) {
            return true; // No hay cambio de estado, es válido.
        }
        switch (estadoActual) {
            case PENDIENTE:
                return nuevoEstado == EstadoTarea.EN_PROGRESO;
            case EN_PROGRESO:
                return nuevoEstado == EstadoTarea.CITA_CONFIRMADA || nuevoEstado == EstadoTarea.COMPLETADA;
            case CITA_CONFIRMADA:
                return nuevoEstado == EstadoTarea.COMPLETADA || nuevoEstado == EstadoTarea.ENVIADA;
            case COMPLETADA:
                return nuevoEstado == EstadoTarea.ENVIADA;
            case ENVIADA:
                return false; // Una vez ENVIADA, no se puede cambiar más el estado (final)
            default:
                return false;
        }
    }
}