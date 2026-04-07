```bash
safezone-backend/
├── 📁 src/main/java/com/safezone/
│   │
│   ├── 🌐 web/                        # CAPA DE ENTRADA (API REST)
│   │   └── 🎮 controller/             # Controladores que reciben las peticiones [3]
│   │       ├── AuthController.java
│   │       ├── UserController.java
│   │       └── CaseController.java
│   │
│   ├── 🧩 domain/                     # CAPA DE DOMINIO (Lógica de Negocio) [1]
│   │   ├── 📥 dto/                    # Objetos para transporte de datos [3]
│   │   ├── 📋 service/                # Interfaces y lógica de negocio [3]
│   │   └── 🗄️ repository/              # Especificación/Interfaces de repositorios [3]
│   │
│   ├── 💾 persistence/                # CAPA DE PERSISTENCIA (Base de Datos) [3]
│   │   ├── 📄 entity/                 # Clases mapeadas con @Entity [3], [4]
│   │   └── 🛠️ crud/                   # Repositorios que extienden JpaRepository [3], [5]
│   │
│   ├── ⚙️ config/                     # Configuraciones transversales (Seguridad, Azure, Mappers)
│   ├── 🚨 exception/                  # Gestión global de errores y excepciones personalizadas
│   └── 📦 common/                     # Elementos compartidos (Enums, Utils)
│
└── 🧪 src/test/java/com/safezone/
    ├── 🧩 modules/                # Pruebas Unitarias espejo por cada módulo
    │   └── 📂 case/
    │       ├── 🧪 CaseServiceTest.java      # Mockito (TDD - Lógica de Negocio)
    │       └── 🧪 CaseControllerTest.java   # MockMvc (TDD - Endpoints)
    └── 🏛️ architecture/           # Pruebas de cumplimiento de reglas de arquitectura
```