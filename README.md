safezone-backend/
├── src/main/java/com/safezone/
│
│ ├── config/                  # Configuraciones transversales
│ │ ├── security/              # JWT & filtros de autenticación
│ │ ├── azure/                 # Configuración Cosmos DB & Blob Storage
│ │ └── mapper/                # ModelMapper o MapStruct
│ │
│ ├── exception/               # Manejo global de errores
│ │ ├── GlobalExceptionHandler.java
│ │ ├── ErrorResponse.java     # DTO estandarizado para errores
│ │ └── custom/                # Excepciones personalizadas
│ │
│ ├── common/                  # Elementos compartidos
│ │ ├── enums/                 # CaseStatus, UrgencyLevel, Role
│ │ └── util/                  # SecurityUtils, DateFormatter
│ │
│ └── modules/                 # Lógica de negocio por dominio
│
│   ├── auth/                  # Autenticación (Login, Registro, JWT)
│   ├── user/                  # Gestión de usuarios
│   └── case/                  # Núcleo del sistema (Denuncias)
│       ├── controller/        # Endpoints REST
│       ├── service/           # Interfaces de negocio
│       ├── service/impl/      # Implementaciones
│       ├── repository/        # Acceso a datos (CosmosRepository)
│       ├── dto/
│       │   ├── request/       # Validación de entrada
│       │   └── response/      # Respuestas sin datos sensibles
│       ├── model/             # Entidades (@Container)
│       └── mapper/            # Conversión Entity ↔ DTO
│
├── src/test/java/com/safezone/
│
│ ├── modules/                 # Pruebas unitarias por módulo
│ │   └── case/
│ │       ├── CaseServiceTest.java     # Mockito (TDD)
│ │       └── CaseControllerTest.java  # MockMvc
│ │
│ └── architecture/            # (Opcional) pruebas de arquitectura