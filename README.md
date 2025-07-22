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
📝 Endpoints de la API
Todas las peticiones deben realizarse a través del API Gateway (http://localhost:8080).

Authentication Service

POST /auth/register: Registra un nuevo usuario.

POST /auth/login: Autentica a un usuario y devuelve un token JWT.


Task Service (Rutas protegidas)

POST /api/v1/tareas: Crea una nueva tarea.


GET /api/v1/tareas/{id}: Obtiene una tarea por su ID.


GET /api/v1/tareas: Obtiene una lista de todas las tareas.


PUT /api/v1/tareas/{id}: Actualiza una tarea existente.


DELETE /api/v1/tareas/{id}: Elimina una tarea.


PATCH /api/v1/tareas/{id}/estado/{nuevoEstado}: Cambia el estado de una tarea.


GET /api/v1/tareas/filtrar: Filtra tareas por estado, prioridad, tipo, etc.


GET /api/v1/tareas/{tareaId}/historial: Obtiene el historial de cambios de una tarea.
