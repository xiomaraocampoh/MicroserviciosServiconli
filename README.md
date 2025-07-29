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

| Método | Endpoint    | Descripción             | Body/Params              |
| ------ | ----------- | ----------------------- | ------------------------ |
| POST   | `/register` | Registrar nuevo usuario | `{ username, password }` |
| POST   | `/login`    | Obtener token JWT       | `{ username, password }` |



Task Service (Rutas protegidas)

| Método | Endpoint                     | Descripción                                      |
| ------ | ---------------------------- | ------------------------------------------------ |
| GET    | `http://localhost:8080/api/v1/tareas`                          | Obtener todas las tareas                         |
| GET    | `http://localhost:8080/api/v1/tareas/{id}`                      | Obtener una tarea por ID                         |
| POST   | `http://localhost:8080/api/v1/tareas`                          | Crear una nueva tarea (cotizante o beneficiario) |
| DELETE | `http://localhost:8080/api/v1/tareas/{id}`                      | Eliminar una tarea                               |
| PATCH  | `http://localhost:8080/api/v1/tareas/3/estado/EN_PROGRESO` | Cambiar estado de la tarea (progresivamente)     |



ejemplo body crear tarea: 

{
"tipo": "Agendar Especialista",
"paciente": "",
"eps": "Sura",
"prioridad": "ALTA",
"observaciones": "Consulta médica optometría",
"tipoPaciente": "COTIZANTE",
"tipoIdentificacionPaciente": "CC",
"numeroIdentificacionPaciente": "12345678",
"fechaExpedicion": "",
"celularPaciente": "31163388640",
"numeroAutorizacion": "",
"numeroRadicado": "",
"especificaciones": "Especificaciones médicas",
"fechaRecordatorio": "2025-07-28T12:00:00",
"telefono": "31163388640"",
"doctor": "Dra. López",
"ubicacion": "Centro Médico",
"fecha": "2025-07-30",
"hora": "10:00"
}



Task Service (pacientes / cotizantes)

| Método | Endpoint                                          | Descripción                          |
|--------|---------------------------------------------------|--------------------------------------|
| POST   | http://localhost:8080/api/v1/pacientes/cotizante  | crea cotizante                       |
| GET    |`http://localhost:8080/api/v1/pacientes/cotizantes`| Obtener todos los cotizantes         |
| GET    |`http://localhost:8080/api/v1/pacientes/cotizantes/{id}` | Obtener cotizante por ID o documento |
| GET    | `http://localhost:8080/api/v1/pacientes/cotizantescotizante?name={nombre}`| Buscar cotizantes por nombre         |

ejemplo crear cotizante: 

{
"nombreCompleto": "Pedro Gómez",
"tipoIdentificacion": "CC",
"numeroIdentificacion": "1231231234",
"fechaExpedicion": "2015-04-21",
"celular": "3004567890",
"eps": "SURA"
}


| Método | Endpoint                                                              | Descripción                             |
|--------|-----------------------------------------------------------------------|-----------------------------------------|
| GET    | `http://localhost:8080/api/v1/pacientes/beneficiarios`                | Obtener todos los beneficiarios         |
| GET    | `http://localhost:8080/api/v1/pacientes/beneficiarios/{id}`           | Obtener beneficiario por ID o documento |
| GET    | `http://localhost:8080/api/v1/pacientes/beneficiario?nombre={NOMBRE}` | Buscar beneficiarios por nombre         |
| Post   | http://localhost:8080/api/v1/pacientes/beneficiario                   | crear beneficiario                      |


ejemplo crear beneficiario: 

{
"nombreCompleto": "María Ramírez",
"tipoIdentificacion": "CC",
"numeroIdentificacion": "3216549874",
"fechaExpedicion": "2019-01-15",
"celular": "3012345678",
"parentesco": "Hija",
"eps": "Sura",
"nombreCotizante": "Pedro Gómez",
"tipoIdentificacionCotizante": "CC",
"numeroIdentificacionCotizante": "1231231234"
}

