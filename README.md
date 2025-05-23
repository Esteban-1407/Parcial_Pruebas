#  Products App - Backend

Este es el backend de **Products App**, una aplicación web CRUD para gestionar productos. Está desarrollada con **Spring Boot** y utiliza **PostgreSQL** como base de datos.
**En la carpeta resources se encuentra el documento con las pruebas no funcionales** 

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
- `entities/` – Entidad JPA `Product`.
- `services/` – Lógica de negocio (servicio y repositorio).
- `repositories/` – Interfaz de acceso a datos JPA.
- `tests/` – Pruebas unitarias e integradas con JUnit y MockMvc.

---

##  Endpoints disponibles

| Método | Endpoint         | Descripción                         |
|--------|------------------|-------------------------------------|
| GET    | `/products`      | Listar todos los productos          |
| GET    | `/products/{id}` | Obtener un producto por su ID       |
| POST   | `/products`      | Crear un nuevo producto             |
| PUT    | `/products/{id}` | Actualizar un producto existente    |
| DELETE | `/products/{id}` | Eliminar un producto por su ID      |

---
 Pruebas
Ejecuta las pruebas con Maven:

bash
Copiar
Editar
./mvnw test
Incluye pruebas:

 Unitarias (ProductServiceImplTest)

 Integración (ProductControllerIntegrationTest)

 CORS
Se permite acceso desde el frontend local de Angular (http://localhost:4200) usando:
@CrossOrigin("http://localhost:4200")

