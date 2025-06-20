
---

##  Tecnologías utilizadas

- Java 17+
- Spring Boot
    - Spring Web
    - Spring Data JPA
- PostgreSQL
- Maven
- JUnit 5 y Spring Test (pruebas integradas y unitarias)
- Mockito (para pruebas de servicios)

---

##  Estructura del Proyecto

- `controllers/` – Controladores REST (endpoints de productos).
- `entities/` – Entidad JPA `Product`, `Categories`, `MovimientoStock`
- `services/` – Lógica de negocio (servicio y repositorio).
- `repositories/` – Interfaz de acceso a datos JPA.
- `tests/` – Pruebas unitarias e integradas con JUnit y MockMvc.

---


 Pruebas
Ejecuta las pruebas con Maven:

bash
Copiar
Editar
./mvnw test
Incluye pruebas:

 Unitarias

 Integración 

 Para pruebas de seguridad se uso PMD el cual analiza las vulnerabilidades de las dependencias y realiza el analisis estatico del codigo. Este reporte se genera en html en la carpeta target, dentro en una carpeta llamada site y en un archivo llamado pmd.xml

