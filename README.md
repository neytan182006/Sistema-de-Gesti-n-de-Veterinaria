# Sistema de Gestión de Veterinaria

Gestión de mascotas, citas e historial médico (Java + MySQL), Proyecto Integrador.

## Funcionalidades

- Listar mascotas con su dueño.
- Agendar cita veterinaria.
- Atender cita: la marca `ATENDIDA` **y** registra diagnóstico/tratamiento en el historial médico, en una sola transacción.
- Ver citas pendientes.
- Ver historial médico completo de una mascota.

## Estructura

```
src/
├── dao/ConexionBD.java, MascotaDAO.java, CitaVeterinariaDAO.java
└── app/Main.java
```

## Base de datos

[`database/veterinaria.sql`](database/veterinaria.sql): `DUENOS`, `MASCOTAS`, `CITAS_VETERINARIAS`, `HISTORIAL_MEDICO`.

## Cómo ejecutarlo

```bash
mysql -u root -p < database/veterinaria.sql
javac -d bin -cp "lib/mysql-connector-j-9.5.0.jar" src/dao/*.java src/app/*.java
java -cp "bin;lib/mysql-connector-j-9.5.0.jar" app.Main
```

> Compilado y verificado con `javac` sin errores; conexión real a MySQL no probada en este entorno (sin servidor corriendo, como acordamos).

## Capturas

_Pendiente: agregar capturas en `capturas/`._

## Licencia

MIT — ver [LICENSE](LICENSE).
