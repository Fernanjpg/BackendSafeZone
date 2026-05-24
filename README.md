# SafeZone Backend

## Informe de avance
SafeZone Backend es la API REST del sistema de protección SafeZone, desarrollada con Spring Boot para gestionar autenticación, denuncias, alertas de emergencia con GPS y administración de usuarios. Esta entrega consolida el núcleo funcional del sistema, mantiene ejecución local para pruebas con el frontend y deja la base lista para una futura migración a nube.

## Resumen ejecutivo
Durante esta fase se reforzó la base técnica del proyecto con autenticación JWT, CRUD profesional de usuarios, manejo de alertas de emergencia y documentación interactiva con Swagger/OpenAPI. El enfoque estuvo en mantener un diseño limpio, validable y compatible con el flujo local de desarrollo.

## Cuadro general de avances por sprint
| Sprint | Nombre | Requerimientos funcionales | Avance estimado |
|---|---|---|---|
| Sprint 1 (Avance Final 2) | Core del Sistema y Gestión de Accesos | RF-01, RF-02, RF-03, RF-04, RF-09 | 100% |
| Sprint 2 (Avance Final 3) | Seguimiento Multidisciplinario | RF-05, RF-06, RF-08, RF-11 | 0% |
| Sprint 3 (Proyecto Final) | Módulos Complementarios y Despliegue | RF-07, RF-10, RF-12, despliegue y optimización | 0% |

### Estado por requerimiento funcional
| RF | Definición | Sprint | Estado actual |
|---|---|---|---|
| RF-01 | Gestión de Denuncias: formulario principal para la creación del expediente. | Sprint 1 | Completado |
| RF-02 | Registro de información de violencia: tipo de violencia y descripción del incidente. | Sprint 1 | Completado |
| RF-03 | Registro de ubicación: geolocalización GPS y entrada manual. | Sprint 1 | Completado |
| RF-04 | Control de Acceso (RBAC): autenticación JWT para los roles Víctima, Psicólogo y Defensor. | Sprint 1 | Completado |
| RF-05 | Control de Acceso a Información Sensible: el especialista solo ve datos del caso asignado. | Sprint 2 | Pendiente |
| RF-06 | Activación del Botón de Pánico: alerta de emergencia y redirección a web neutra. | Sprint 2 | En planificación |
| RF-07 | Chat de Mensajería Multidisciplinario: comunicación interna cifrada entre actores. | Sprint 3 | Pendiente |
| RF-08 | Consulta de Estado del Caso: línea de tiempo del progreso de la denuncia para la víctima. | Sprint 2 | Pendiente |
| RF-09 | Asignación de Casos (Workflow): el administrador deriva la denuncia a los especialistas. | Sprint 1 | Completado |
| RF-10 | Agenda de Atención y Audiencias: calendario de citas y fechas legales. | Sprint 3 | Pendiente |
| RF-11 | Bitácora de Seguimiento: registro de notas cronológicas por psicólogo y defensor. | Sprint 2 | Pendiente |
| RF-12 | Repositorio de Evidencias Cifrado: carga y gestión de archivos con acceso restringido. | Sprint 3 | Pendiente |

### Detalle del Sprint 1 de esta entrega
| N° | Requerimiento | Definición | Estado | Detalles agregados para cumplirlo |
|---|---|---|---|---|
| 01 | RF-01 | Gestión de Denuncias | Completado | Se estructuró el flujo base de denuncias y el expediente para el registro principal. |
| 02 | RF-02 | Registro de información de violencia | Completado | Se incorporó el registro del tipo de violencia y la descripción del incidente. |
| 03 | RF-03 | Registro de ubicación | Completado | Se habilitó el envío de ubicación GPS y la entrada manual, junto con geocodificación inversa. |
| 04 | RF-04 | Control de Acceso (RBAC) | Completado | Se implementó autenticación JWT y control por roles Víctima, Psicólogo y Defensor. |
| 05 | RF-09 | Asignación de Casos (Workflow) | Completado | Se organizó el flujo para derivación de denuncias a especialistas desde el backend. |

### Alcance del sprint 1
- El sprint consolida el núcleo funcional del sistema.
- Se priorizó autenticación, registro de denuncias y ubicación.
- Se dejó una base limpia para escalar a seguimiento multidisciplinario y módulos complementarios.

## Entregables incluidos
- Registro e inicio de sesión con JWT.
- CRUD profesional de usuarios con DTOs de respuesta.
- Alertas de emergencia con ubicación y geocodificación inversa.
- Integración de Swagger/OpenAPI para pruebas y documentación.
- Estructura compatible con pruebas locales y evolución futura a nube.

## Guía de usuario rápida
### 1. Registro
El sistema solicita nombre, apellido, email, contraseña, teléfono, rol y región cuando aplique.

### 2. Inicio de sesión
El login utiliza exactamente los campos siguientes:
```json
{
  "email": "usuario@correo.com",
  "password": "TuClaveSegura123!"
}
```
La respuesta incluye un JWT que debe usarse en las rutas protegidas.

### 3. Creación de alerta de emergencia
Desde el frontend, el botón de pánico envía la ubicación GPS y, si está disponible, la dirección obtenida por geocodificación inversa.

### 4. Seguimiento y atención
Los perfiles autorizados pueden consultar alertas activas, tomar un caso y resolverlo desde la API o desde Swagger.

## Swagger / OpenAPI
La documentación interactiva está disponible cuando el backend está en ejecución:
- http://localhost:8080/swagger-ui.html
- http://localhost:8080/v3/api-docs

Pasos rápidos para usar Swagger:
1. Abrir la interfaz en el navegador.
2. Autenticarse con el botón Authorize si la operación lo requiere.
3. Probar primero `POST /api/auth/register` o `POST /api/auth/login`.
4. Copiar el token JWT y usarlo en las rutas protegidas.
5. Validar alertas, usuarios y geocodificación desde la interfaz.

Desde Swagger se pueden probar los principales endpoints:
- POST /api/auth/register
- POST /api/auth/login
- GET /api/users
- POST /api/emergency/alerts
- GET /api/emergency/alerts
- PATCH /api/emergency/alerts/{id}/attend
- PATCH /api/emergency/alerts/{id}/resolve
- GET /api/emergency/geocode

## Ejecución local
### Backend
```bash
./mvnw spring-boot:run
```

### Frontend
```bash
npm install
npm run dev
```

## Requisitos del entorno
- Java 25
- Maven Wrapper incluido en el repositorio
- Backend en http://localhost:8080
- Frontend Vite en http://localhost:5173

## Observaciones técnicas
- La seguridad se gestiona con JWT y BCrypt.
- CORS está habilitado para el frontend local durante desarrollo.
- Las credenciales y valores sensibles deben externalizarse antes de un despliegue en nube.
- La solución actual permite seguir probando localmente sin comprometer una futura publicación en producción.

## Validación
La base de pruebas del backend fue ejecutada correctamente con Maven y se mantuvieron los resultados esperados de forma consistente.