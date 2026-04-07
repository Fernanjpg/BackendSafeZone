safezone-backend/
├── 📁 src/main/java/com/safezone/
│   │
│   ├── ⚙️ config/                 # Configuraciones transversales
│   │   ├── 🔐 security/           # JWT, Filtros y CustomUserDetailsService
│   │   ├── ☁️ azure/              # Configuración de Cosmos DB & Blob Storage
│   │   └── 🔄 mapper/              # Configuración de ModelMapper o MapStruct
│   │
│   ├── 🚨 exception/              # Gestión Global de Errores
│   │   ├── 🌐 GlobalExceptionHandler.java
│   │   ├── 📄 ErrorResponse.java      # DTO estandarizado para respuestas de error
│   │   └── 🛠️ custom/                 # Excepciones de negocio (BusinessException, etc.)
│   │
│   ├── 📦 common/                 # Elementos compartidos por todos los módulos
│   │   ├── 🏷️ enums/               # CaseStatus, UrgencyLevel, Role
│   │   └── 🛠️ util/                # SecurityUtils, DateFormatter, Validators
│   │
│   └── 🧩 modules/                # LÓGICA DE NEGOCIO POR DOMINIO (CORE)
│       ├── 🔑 auth/               # Gestión de Sesiones y Seguridad
│       ├── 👤 user/               # Administración de perfiles (Víctima, Especialista)
│       └── 📂 case/               # Núcleo del sistema: Gestión de Denuncias
│           ├── 🎮 controller/     # Endpoints REST expuestos
│           ├── 📋 service/        # Contratos de lógica de negocio (Interfaces)
│           ├── 🛠️ service/impl/   # Implementación detallada de la lógica
│           ├── 🗄️ repository/     # Capa de datos (Azure Cosmos DB)
│           ├── 📥 dto/request/    # Objetos de entrada con validaciones (Bean Validation)
│           ├── 📤 dto/response/   # Objetos de salida protegidos (Privacy-First)
│           ├── 📄 model/          # Documentos/Entidades (@Container)
│           └── 🔄 mapper/          # Mapeo específico Entity ↔ DTO
│
└── 🧪 src/test/java/com/safezone/
    ├── 🧩 modules/                # Pruebas Unitarias espejo por cada módulo
    │   └── 📂 case/
    │       ├── 🧪 CaseServiceTest.java      # Mockito (TDD - Lógica de Negocio)
    │       └── 🧪 CaseControllerTest.java   # MockMvc (TDD - Endpoints)
    └── 🏛️ architecture/           # Pruebas de cumplimiento de reglas de arquitectura