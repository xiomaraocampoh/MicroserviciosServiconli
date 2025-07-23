package com.serviconli.reportservice.service;

import com.serviconli.reportservice.dto.TareaReporteDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio encargado de generar un archivo Excel a partir de una lista de tareas.
 */
@Service
public class ExcelGeneratorService {

    /**
     * Crea un archivo Excel en un stream de bytes a partir de la lista de tareas.
     *
     * @param tareas La lista de objetos TareaReporteDTO con los datos a exportar.
     * @return Un ByteArrayOutputStream que contiene el archivo Excel generado.
     * @throws IOException Si ocurre un error durante la escritura del workbook.
     */
    public ByteArrayOutputStream generarReporteExcel(List<TareaReporteDTO> tareas) throws IOException {
        // Definimos el formato para las fechas en el Excel
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Creamos un nuevo libro de trabajo de Excel (formato .xlsx)
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Creamos una nueva hoja en el libro
            Sheet sheet = workbook.createSheet("Reporte de Tareas");

            // --- Estilo para la cabecera ---
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // --- Creación de la fila de cabecera ---
            String[] columnas = {"ID", "Tipo", "Paciente", "EPS", "Prioridad", "Estado", "Observaciones", "Fecha Creación", "Fecha Recordatorio", "Asignado A"};
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // --- Llenado de los datos de las tareas ---
            int rowIdx = 1;
            for (TareaReporteDTO tarea : tareas) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(tarea.getId() != null ? tarea.getId() : 0);
                row.createCell(1).setCellValue(tarea.getTipo());
                row.createCell(2).setCellValue(tarea.getPaciente());
                row.createCell(3).setCellValue(tarea.getEps());
                row.createCell(4).setCellValue(tarea.getPrioridad());
                row.createCell(5).setCellValue(tarea.getEstado());
                row.createCell(6).setCellValue(tarea.getObservaciones());

                // Formateamos las fechas para que se muestren correctamente
                row.createCell(7).setCellValue(tarea.getFechaCreacion() != null ? tarea.getFechaCreacion().format(formatter) : "");
                row.createCell(8).setCellValue(tarea.getFechaRecordatorio() != null ? tarea.getFechaRecordatorio().format(formatter) : "");
                row.createCell(9).setCellValue(tarea.getAsignadoA());
            }

            // --- Ajustar el tamaño de las columnas automáticamente ---
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Escribimos el contenido del libro de trabajo en el stream de salida
            workbook.write(out);
            return out;
        }
    }
}