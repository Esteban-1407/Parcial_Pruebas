# language: es
Característica: Gestión de Productos API REST
  Como desarrollador
  Quiero gestionar productos a través de una API REST
  Para mantener el catálogo de productos actualizado

  Antecedentes:
    Dado que el servidor está en funcionamiento

  Escenario: Obtener lista vacía de productos
    Dado que no hay productos en el sistema
    Cuando solicito la lista de productos
    Entonces debería recibir una respuesta con código 200
    Y la lista de productos debería estar vacía

  Escenario: Crear un nuevo producto
    Cuando creo un producto con los siguientes datos:
      | campo       | valor               |
      | name        | Laptop Gaming       |
      | description | Laptop para juegos  |
      | price       | 1500                |
    Entonces debería recibir una respuesta con código 201
    Y el producto creado debería tener un ID asignado
    Y el producto debería tener nombre "Laptop Gaming"

  Escenario: Obtener lista con productos
    Dado que existen los siguientes productos:
      | name          | description         | price |
      | Mouse Gamer   | Mouse inalámbrico   | 50    |
      | Teclado RGB   | Teclado mecánico    | 120   |
    Cuando solicito la lista de productos
    Entonces debería recibir una respuesta con código 200
    Y la lista debería contener 2 productos

  Escenario: Obtener detalles de un producto específico
    Dado que existe un producto con nombre "Monitor 4K"
    Cuando solicito los detalles del producto
    Entonces debería recibir una respuesta con código 200
    Y el producto debería tener nombre "Monitor 4K"

  Escenario: Actualizar un producto existente
    Dado que existe un producto con nombre "Webcam HD"
    Cuando actualizo el producto con los siguientes datos:
      | campo       | valor            |
      | name        | Webcam HD Pro    |
      | description | Webcam mejorada  |
      | price       | 150              |
    Entonces debería recibir una respuesta con código 201
    Y el producto actualizado debería tener nombre "Webcam HD Pro"

  Escenario: Eliminar un producto existente
    Dado que existe un producto con nombre "Auriculares Bluetooth"
    Cuando elimino el producto
    Entonces debería recibir una respuesta con código 200
    Y el producto eliminado debería tener nombre "Auriculares Bluetooth"
    Y el producto no debería existir en el sistema

  Escenario: Intentar obtener un producto que no existe
    Cuando solicito los detalles del producto con ID 9999
    Entonces debería recibir una respuesta con código 404

  Escenario: Intentar actualizar un producto que no existe
    Cuando intento actualizar el producto con ID 9999
    Entonces debería recibir una respuesta con código 404

  Escenario: Intentar eliminar un producto que no existe
    Cuando intento eliminar el producto con ID 9999
    Entonces debería recibir una respuesta con código 404