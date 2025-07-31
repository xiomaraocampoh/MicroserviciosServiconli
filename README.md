# ServiSoft: Microservicio de Gestión de Tareas para Central de Citas - Serviconli

# 📖 Sobre el Proyecto
ServiSoft: 
es una aplicación backend construida con una arquitectura de microservicios. El sistema está diseñado para gestionar tareas de una empresa de servicios, incluyendo la autenticación de usuarios, la gestión de tareas y un API Gateway que centraliza y protege las comunicaciones.
El proyecto está compuesto por los siguientes microservicios:
auth-service: Servicio encargado de la autenticación y autorización. Gestiona el registro y login de usuarios, generando un JSON Web Token (JWT) para asegurar las comunicaciones.
task-service: Gestiona toda la lógica de negocio relacionada con las tareas. Permite operaciones CRUD (Crear, Leer, Actualizar, Eliminar), cambiar estados, filtrar y consultar el historial de cambios de una tarea.
api-gateway: Punto de entrada único para todas las peticiones del cliente. Enruta las solicitudes a los microservicios correspondientes, gestiona la configuración de CORS y valida los tokens JWT para proteger las rutas.

# 🛠️ Tecnologías Utilizadas
Este proyecto está construido con las siguientes tecnologías:
Lenguaje: Java 17 
Framework: Spring Boot 3 
Base de Datos: MySQL 

# Arquitectura:
Spring Cloud Gateway  (para el api-gateway)
Spring Data JPA (Hibernate)  (para la persistencia de datos)
Spring Security  (para la seguridad)
Autenticación: JSON Web Tokens (JWT) 
Gestión de Dependencias: Maven 


# Otras librerías:

Lombok 
JJwt (Java JWT) 

# 📂 Estructura del Proyecto
El proyecto está organizado en tres módulos de Maven, cada uno correspondiente a un microservicio:

MicroserviciosServiconli/
├── auth-service/           # Servicio de autenticación
│   ├── src/main/java/
│   └── pom.xml
├── task-service/           # Servicio de gestión de tareas
│   ├── src/main/java/
│   └── pom.xml
└── apigateway/             # API Gateway
    ├── src/main/java/
    └── pom.xml

📝 Endpoints de la API ( para probar en postman)

headers necesarios: 
Authorization: Bearer <token_obtenido_en_login>
Content-Type: application/json

Todas las peticiones deben realizarse a través del API Gateway (http://localhost:8080).

Authentication Service:

| Método | Endpoint                              | Descripción             | Body/Params              |
| ------ |---------------------------------------| ----------------------- | ------------------------ |
| POST   | `http://localhost:8080/auth/register` | Registrar nuevo usuario | `{ email, password }` |
| POST   | `http://localhost:8080/auth/login`    | Obtener token JWT       | `{ email, password }` |

ejemplo registras: 
{
  "email": "ejemplo@gruposerviconli.com",
  "password": "1234567"
}
 debe si o si tener un dominio @gruposerviconli.com

T📡 Endpoints task-service
Todos los endpoints requieren autorización con JWT (Authorization: Bearer <token>).

🔹 Gestión de Tareas

| Método   | Endpoint                                                   | Descripción                                          |
| -------- | ---------------------------------------------------------- | ---------------------------------------------------- |
| `GET`    | `http://localhost:8080/api/v1/tareas`                                           | Obtener todas las tareas                             |
| `GET`    | `/api/v1/tareas/{id}`                                      | Obtener una tarea por ID                             |
| `POST`   | `/api/v1/tareas`                                           | Crear una nueva tarea                                |
| `PUT`    | `/api/v1/tareas/{id}`                                      | Actualizar una tarea existente                       |
| `DELETE` | `/api/v1/tareas/{id}`                                      | Eliminar una tarea por ID                            |
| `PUT`    | `/api/v1/tareas/{id}/estado`                               | Cambiar el estado de una tarea (de forma progresiva) |
| `GET`    | `/api/v1/tareas/filtrar?estado=EN_PROGRESO&prioridad=ALTA` | Filtrar tareas por estado y prioridad                |

Ejemplo POST /api/v1/tareas
json
Copiar código
{
"tipo": "Cita médica",
"paciente": "Carlos Ramírez",
"eps": "SURA",
"prioridad": "ALTA",
"estado": "PENDIENTE",
"observaciones": "Paciente solicita exámenes previos.",
"telefono": "3214567890",
"doctor": "Dra. Gómez",
"ubicacion": "Consultorio 3",
"fecha": "2025-08-01",
"hora": "09:30",
"tipoPaciente": "BENEFICIARIO",
"tipoIdentificacionPaciente": "CC",
"numeroIdentificacionPaciente": "1234567890",
"fechaExpedicion": "2015-04-20",
"celularPaciente": "3214567890",
"parentezco": "Hijo",
"nombreCotizante": "Luis Ramírez",
"numeroIdentificacionCotizante": "987654321",
"numeroAutorizacion": "A-102938",
"numeroRadicado": "R-483920",
"especificaciones": "Ayuno de 12 horas",
"fechaRecordatorio": "2025-07-31T09:00:00"
}
🛠️ Ejemplo PUT /api/v1/tareas/{id}
json
Copiar código
{
"tipo": "Reprogramación de cita",
"paciente": "Carlos Ramírez",
"eps": "SURA",
"prioridad": "MEDIA",
"estado": "EN_PROGRESO",
"observaciones": "Cambio de fecha por vacaciones.",
"telefono": "3214567890",
"doctor": "Dra. Gómez",
"ubicacion": "Consultorio 5",
"fecha": "2025-08-05",
"hora": "14:00",
"tipoPaciente": "BENEFICIARIO",
"tipoIdentificacionPaciente": "CC",
"numeroIdentificacionPaciente": "1234567890",
"fechaExpedicion": "2015-04-20",
"celularPaciente": "3214567890",
"parentezco": "Hijo",
"nombreCotizante": "Luis Ramírez",
"numeroIdentificacionCotizante": "987654321",
"numeroAutorizacion": "A-102938",
"numeroRadicado": "R-483920",
"especificaciones": "No requiere exámenes",
"fechaRecordatorio": "2025-08-04T13:00:00"
}


