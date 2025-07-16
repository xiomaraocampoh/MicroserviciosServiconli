package com.serviconli.task.repository;

import com.serviconli.task.model.HistorialTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialTareaRepository extends JpaRepository<HistorialTarea, Long> {
    List<HistorialTarea> findByTareaIdOrderByFechaCambioAsc(Long tareaId);
}