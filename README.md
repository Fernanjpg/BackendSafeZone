safezone-backend/
├── src/main/java/com/safezone/
│ ├── config/ # Configuraciones transversales
│ │ ├── security/ # JWT & Auth Filters
│ │ ├── azure/ # CosmosDB & Blob Storage Config
│ │ └── mapper/ # ModelMapper o MapStruct
│ │
│ ├── exception/ # Manejo Global (Advice)
│ │ ├── GlobalExceptionHandler.java
│ │ ├── ErrorResponse.java # DTO estandarizado para errores
│ │ └── custom/ # Excepciones propias (UnauthorizedException, etc.)
│ │
│ ├── common/ # Lo que comparten todos los módulos
│ │ ├── enums/ # CaseStatus, UrgencyLevel, Role
│ │ └── util/ # SecurityUtils, DateFormatter
│ │
│ └── modules/ # LÓGICA DE NEGOCIO POR DOMINIO
│ ├── auth/ # Login, Registro, Refresh Token
│ ├── user/ # Gestión de perfiles (Víctima, Psicólogo, Defensor)
│ └── case/ # El núcleo del sistema (Denuncias)
│ ├── controller/ # Endpoints
│ ├── service/ # Interfaces de lógica
│ ├── service/impl/ # Implementación (aquí va el grueso del código)
│ ├── repository/ # CosmosRepository
│ ├── dto/request/ # Validaciones de entrada
│ ├── dto/response/ # Proyecciones de salida (sin datos sensibles)
│ ├── model/ # @Container (Entidad de Cosmos DB)
│ └── mapper/ # Conversión Entity <-> DTO
│
├── src/test/java/com/safezone/
│ ├── modules/ # Pruebas unitarias espejo por cada módulo
│ │ └── case/
│ │ ├── CaseServiceTest.java # Mockito (TDD)
│ │ └── CaseControllerTest.java # MockMvc
│ └── architecture/ # Pruebas de arquitectura (ArchUnit opcional)
