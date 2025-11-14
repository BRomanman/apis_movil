# APIs móviles de la clínica

Este repositorio contiene los microservicios que usa la app móvil de la clínica para administrar usuarios, doctores, historiales médicos, citas y seguros. Cada servicio es independiente, pero comparten un estilo REST y convenciones de respuesta coherentes (200 para lecturas exitosas, 201 en creaciones, 204 cuando no hay contenido y 4xx ante errores de negocio).

> Los ejemplos a continuación usan `curl` contra puertos de desarrollo sugeridos. Ajusta host/puerto según el servicio que estés ejecutando.

## Servicios disponibles

| Servicio      | Carpeta     | Base URL sugerida                    | Funcionalidad principal |
|--------------|-------------|--------------------------------------|-------------------------|
| HistorialAPI | `HistorialAPI` | `http://localhost:8083/api/v1/historial` | Consulta historiales clínicos |
| CitasAPI     | `CitasAPI`     | `http://localhost:8082/api/v1/citas`     | CRUD de citas médicas |
| SegurosAPI   | `SegurosAPI`   | `http://localhost:8084/api/v1/seguros`   | Gestión de seguros médicos |
| UsuariosAPI  | `UsuariosAPI`  | `http://localhost:8081/api/v1`           | Usuarios, doctores y login |

---

## HistorialAPI
Permite consultar historiales médicos ya registrados. No expone creación/edición porque esos datos provienen de otros flujos clínicos.

### `GET /usuario/{usuarioId}` – Historiales por usuario
Devuelve todos los historiales asociados a un paciente. Responde `204 No Content` cuando la lista está vacía.
```bash
curl -X GET "http://localhost:8083/api/v1/historial/usuario/15"
```
**Respuesta 200**
```json
[
  {
    "id": 71,
    "usuario": {
      "id": 15,
      "nombre": "Laura",
      "apellido": "Rivas",
      "correo": "laura.rivas@example.com"
    },
    "fechaConsulta": "2024-09-10",
    "diagnostico": "Hipertensión controlada",
    "observaciones": "Paciente estable, control en 3 meses"
  }
]
```

### `GET /{id}` – Historial específico
Consulta un historial concreto; si no existe, se responde 404.
```bash
curl -X GET "http://localhost:8083/api/v1/historial/98"
```
**Respuesta 200**
```json
{
  "id": 98,
  "usuario": {
    "id": 22,
    "nombre": "Pablo",
    "apellido": "Maldonado"
  },
  "fechaConsulta": "2024-11-01",
  "diagnostico": "Gastritis",
  "observaciones": "Controlar dieta y recetar inhibidor de bomba"
}
```

---

## CitasAPI
Gestiona el ciclo completo de las citas médicas. Trabaja con entidades `Cita`, `Usuario` y `Doctor`.

### `GET /` – Listar todas las citas
```bash
curl -X GET "http://localhost:8082/api/v1/citas"
```
**Respuesta 200**
```json
[
  {
    "id": 42,
    "fechaCita": "2025-02-10T09:30:00",
    "estado": "CONFIRMADA",
    "idUsuario": 15,
    "idDoctor": 3,
    "idConsulta": 120
  }
]
```
Si no hay registros, devuelve 204.

### `GET /{id}` – Detalle de cita
```bash
curl -X GET "http://localhost:8082/api/v1/citas/42"
```
**Respuesta 200**
```json
{
  "id": 42,
  "fechaCita": "2025-02-10T09:30:00",
  "estado": "CONFIRMADA",
  "idUsuario": 15,
  "idDoctor": 3,
  "idConsulta": 120
}
```

### `GET /usuario/{idUsuario}` – Citas de un usuario
```bash
curl -X GET "http://localhost:8082/api/v1/citas/usuario/15"
```
**Respuesta 200** (lista filtrada) o 204 si el paciente no tiene citas.

### `POST /` – Crear cita
Si el `id` llega nulo, la capa de servicio marca el estado como `CONFIRMADA` por defecto.
```bash
curl -X POST "http://localhost:8082/api/v1/citas" \
  -H "Content-Type: application/json" \
  -d '{
        "fechaCita": "2025-03-02T11:00:00",
        "estado": "PENDIENTE",
        "idUsuario": 15,
        "idDoctor": 3,
        "idConsulta": 128
      }'
```
**Respuesta 201**
```json
{
  "id": 84,
  "fechaCita": "2025-03-02T11:00:00",
  "estado": "CONFIRMADA",
  "idUsuario": 15,
  "idDoctor": 3,
  "idConsulta": 128
}
```

### `PUT /{id}` – Actualizar cita existente
```bash
curl -X PUT "http://localhost:8082/api/v1/citas/84" \
  -H "Content-Type: application/json" \
  -d '{
        "fechaCita": "2025-03-05T08:30:00",
        "idDoctor": 4,
        "idConsulta": 140
      }'
```
**Respuesta 200** devuelve la cita con los cambios aplicados.

### `DELETE /{id}` – Eliminar cita
```bash
curl -X DELETE "http://localhost:8082/api/v1/citas/84"
```
**Respuesta 204** si la eliminación fue exitosa; 404 si el id no existe.

### `GET /usuario/{idUsuario}/proximas` – Próximas citas
Obtiene solo las que tienen `fechaCita` mayor a `now()`.
```bash
curl -X GET "http://localhost:8082/api/v1/citas/usuario/15/proximas"
```
**Respuesta 200**: misma estructura que la lista general.

---

## SegurosAPI
Ofrece altas, consultas, actualizaciones, cancelaciones y bajas lógicas para seguros médicos. Las reglas de negocio (nombres únicos por usuario, no cancelar dos veces, etc.) se manejan en `SeguroService` y los códigos de error se normalizan en `ApiExceptionHandler`.

### `POST /` – Tomar un seguro
```bash
curl -X POST "http://localhost:8084/api/v1/seguros" \
  -H "Content-Type: application/json" \
  -d '{
        "nombreSeguro": "Plan Platino",
        "descripcion": "Cobertura dental + oftalmológica",
        "usuarioId": 15
      }'
```
**Respuesta 201**
```json
{
  "id": 12,
  "nombreSeguro": "Plan Platino",
  "descripcion": "Cobertura dental + oftalmológica",
  "estado": "ACTIVO",
  "fechaCreacion": "2024-11-13T18:30:12.120987",
  "fechaCancelacion": null,
  "usuarioId": 15,
  "usuarioNombre": "Laura Rivas"
}
```

### `GET /` – Listar seguros
```bash
curl -X GET "http://localhost:8084/api/v1/seguros"
```
Devuelve un arreglo de `SeguroResponse` o 204 si no existen registros.

### `GET /usuario/{usuarioId}` – Seguros por titular
```bash
curl -X GET "http://localhost:8084/api/v1/seguros/usuario/15"
```
Responde 200 con la lista filtrada o 204 si el usuario aún no contrata seguros.

### `GET /{id}` – Seguro específico
```bash
curl -X GET "http://localhost:8084/api/v1/seguros/12"
```
Devuelve el `SeguroResponse` o 404 si no se encuentra.

### `PUT /{id}` – Actualizar seguro
Puede cambiar nombre, descripción o transferir el seguro a otro usuario siempre que no rompa la regla de nombres únicos por titular.
```bash
curl -X PUT "http://localhost:8084/api/v1/seguros/12" \
  -H "Content-Type: application/json" \
  -d '{
        "nombreSeguro": "Plan Platino Plus",
        "descripcion": "Incluye telemedicina",
        "usuarioId": 16
      }'
```
**Respuesta 200** con el seguro actualizado.

### `PATCH /{id}/cancelacion` – Cancelar seguro
El cuerpo es opcional; si se envía `motivo`, se agrega a la descripción como bitácora.
```bash
curl -X PATCH "http://localhost:8084/api/v1/seguros/12/cancelacion" \
  -H "Content-Type: application/json" \
  -d '{ "motivo": "Paciente migra a otro plan" }'
```
**Respuesta 200** muestra `estado="CANCELADO"` y `fechaCancelacion`. Si ya estaba cancelado, devuelve 409.

### `DELETE /{id}` – Borrar seguro
```bash
curl -X DELETE "http://localhost:8084/api/v1/seguros/12"
```
Elimina definitivamente y responde 204. Si el id no existe, el handler devuelve 404.

---

## UsuariosAPI
Expone controladores para usuarios finales (`/api/v1/usuarios`), doctores (`/api/v1/doctores`) y autenticación (`/api/v1/auth`). La capa de servicio bloquea operaciones sobre usuarios con rol `administrador` para proteger cuentas maestras.

### Gestión de usuarios (`/api/v1/usuarios`)

#### `GET /` – Listar usuarios visibles
```bash
curl -X GET "http://localhost:8081/api/v1/usuarios"
```
Responde con `UsuarioResponse` (sin administradores) o 204.

#### `GET /{id}` – Usuario por id
```bash
curl -X GET "http://localhost:8081/api/v1/usuarios/25"
```
Devuelve el usuario si no es administrador; caso contrario responde 404 para evitar filtraciones.

#### `POST /` – Crear usuario
```bash
curl -X POST "http://localhost:8081/api/v1/usuarios" \
  -H "Content-Type: application/json" \
  -d '{
        "nombre": "Ana",
        "apellido": "Guzmán",
        "fechaNacimiento": "1992-05-01T00:00:00",
        "correo": "ana.guzman@example.com",
        "telefono": "+56911112222",
        "contrasena": "clave123",
        "rol": { "id": 2 }
      }'
```
**Respuesta 201**
```json
{
  "id": 25,
  "nombre": "Ana",
  "apellido": "Guzmán",
  "fechaNacimiento": "1992-05-01T00:00:00",
  "correo": "ana.guzman@example.com",
  "telefono": "+56911112222",
  "rol": "paciente",
  "doctor": null
}
```

#### `PUT /{id}` – Actualizar usuario
```bash
curl -X PUT "http://localhost:8081/api/v1/usuarios/25" \
  -H "Content-Type: application/json" \
  -d '{
        "nombre": "Ana Carolina",
        "apellido": "Guzmán",
        "fechaNacimiento": "1992-05-01T00:00:00",
        "correo": "aca.guzman@example.com",
        "telefono": "+56999998888",
        "contrasena": "clave456",
        "rol": { "id": 3 }
      }'
```
Responde 200 con la versión actualizada, siempre que el usuario objetivo y el payload no correspondan a un administrador.

#### `DELETE /{id}` – Eliminar usuario
```bash
curl -X DELETE "http://localhost:8081/api/v1/usuarios/25"
```
Responde 204 si se elimina y 404 si el id no existe o estaba reservado para administración.

### Gestión de doctores (`/api/v1/doctores`)

#### `GET /` – Listar doctores activos
```bash
curl -X GET "http://localhost:8081/api/v1/doctores"
```
Devuelve un arreglo de `DoctorResponse` o 204 si no hay doctores activos (la eliminación se maneja como baja lógica).

#### `GET /{id}` – Detalle de doctor
```bash
curl -X GET "http://localhost:8081/api/v1/doctores/5"
```
Obtiene sueldo, bono, tarifa y datos básicos del usuario asociado.

#### `POST /` – Crear doctor
```bash
curl -X POST "http://localhost:8081/api/v1/doctores" \
  -H "Content-Type: application/json" \
  -d '{
        "tarifaConsulta": 35000,
        "sueldo": 1200000,
        "bono": 200000,
        "usuario": { "id": 18 }
      }'
```
**Respuesta 201** devuelve el `DoctorResponse` resultante.

#### `PUT /{id}` – Actualizar doctor
```bash
curl -X PUT "http://localhost:8081/api/v1/doctores/5" \
  -H "Content-Type: application/json" \
  -d '{
        "tarifaConsulta": 38000,
        "sueldo": 1250000,
        "bono": 250000,
        "usuario": { "id": 18 }
      }'
```
Responde 200 con los nuevos valores. Si el doctor no existe o fue dado de baja, responde 404.

#### `DELETE /{id}` – Baja lógica de doctor
```bash
curl -X DELETE "http://localhost:8081/api/v1/doctores/5"
```
Marca `activo=false` y responde 204.

### Autenticación (`/api/v1/auth`)

#### `POST /login` – Iniciar sesión
```bash
curl -X POST "http://localhost:8081/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
        "correo": "ana.guzman@example.com",
        "contrasena": "clave456"
      }'
```
**Respuesta 200**
```json
{
  "userId": 25,
  "role": "paciente",
  "doctorId": null,
  "nombre": "Ana Carolina",
  "apellido": "Guzmán",
  "correo": "ana.guzman@example.com"
}
```
Si las credenciales son incorrectas, devuelve 401.

---

## Notas finales
- Ajusta los ejemplos a tus datos reales (ids, fechas, roles, etc.).
- Cada servicio usa Spring Boot + JPA; puedes arrancarlos con `./gradlew bootRun` desde su carpeta.
- Ante errores de lógica (duplicados, entidades inexistentes, validaciones), revisa los códigos de estado para manejar los mensajes en la app móvil.
