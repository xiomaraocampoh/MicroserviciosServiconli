package com.serviconli.reportservice.controller;

import com.serviconli.reportservice.dto.TareaReporteDTO;
import com.serviconli.reportservice.service.ExcelGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ExcelGeneratorService excelGeneratorService;

    /**
     * Endpoint para generar un reporte de tareas en formato Excel.
     * Recibe una lista de tareas en el cuerpo de la petición.
     *
     * @param tareas La lista de TareaReporteDTO.
     * @return Un archivo .xlsx para descargar.
     */
    @PostMapping("/excel")
    public ResponseEntity<Resource> exportarAExcel(@RequestBody List<TareaReporteDTO> tareas) {
        try {
            // Generamos el archivo Excel en memoria
            ByteArrayOutputStream stream = excelGeneratorService.generarReporteExcel(tareas);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());

            InputStreamResource resource = new InputStreamResource(inputStream);

            // Nombre dinámico para el archivo
            String filename = "Reporte_Tareas_" + LocalDate.now() + ".xlsx";

            // Configuramos los headers de la respuesta para indicar que es un archivo descargable
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);

        } catch (IOException e) {
            // En caso de un error, devolvemos una respuesta de error del servidor
            // En un caso real, aquí se debería loguear el error.
            return ResponseEntity.status(500).build();
        }
    }
}
