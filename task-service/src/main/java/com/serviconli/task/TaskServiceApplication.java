package com.serviconli.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }

}

// para probar en postman http://localhost:8081/api/v1/tareas o 8080 api gateway {
//    "tipo": "cita",
//    "paciente": "Juan Perez",
//    "eps": "SURA",
//    "prioridad": "ALTA",
//    "observaciones": "Consulta cardiología, verificar historial",
//    "fechaRecordatorio": "2025-07-20T10:00:00"
//}