package org.esteban.springboot.springmvc.app.springbootbackend.steps;

import io.cucumber.java.Before;
import io.cucumber.java.es.*;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.esteban.springboot.springmvc.app.springbootbackend.entities.Product;
import org.esteban.springboot.springmvc.app.springbootbackend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductRepository productRepository;

    private Response response;
    private Product currentProduct;
    private Long currentProductId;
    private RequestSpecification request;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        productRepository.deleteAll();
    }

    @Dado("que el servidor está en funcionamiento")
    public void queElServidorEstaEnFuncionamiento() {
        request = given()
                .contentType("application/json")
                .accept("application/json");
    }

    @Dado("que no hay productos en el sistema")
    public void queNoHayProductosEnElSistema() {
        productRepository.deleteAll();
    }

    @Cuando("solicito la lista de productos")
    public void solicitoLaListaDeProductos() {
        response = request.when().get("/products");
    }

    @Entonces("debería recibir una respuesta con código {int}")
    public void deberiaRecibirUnaRespuestaConCodigo(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Y("la lista de productos debería estar vacía")
    public void laListaDeProductosDeberiaEstarVacia() {
        List<Product> products = response.jsonPath().getList("", Product.class);
        assertTrue(products.isEmpty());
    }

    @Cuando("creo un producto con los siguientes datos:")
    public void creoUnProductoConLosSiguientesDatos(Map<String, String> productData) {
        Product product = new Product();
        product.setName(productData.get("name"));
        product.setDescription(productData.get("description"));
        product.setPrice(Long.parseLong(productData.get("price")));

        response = request
                .body(product)
                .when()
                .post("/products");

        if (response.statusCode() == 201) {
            currentProduct = response.as(Product.class);
            currentProductId = currentProduct.getId();
        }
    }

    @Y("el producto creado debería tener un ID asignado")
    public void elProductoCreadoDeberiaTenerUnIdAsignado() {
        assertNotNull(currentProduct);
        assertTrue(currentProduct.getId() > 0);
    }

    @Y("el producto debería tener nombre {string}")
    public void elProductoDeberiaTenerNombre(String expectedName) {
        assertEquals(expectedName, currentProduct.getName());
    }

    @Dado("que existen los siguientes productos:")
    public void queExistenLosSiguientesProductos(List<Map<String, String>> productsData) {
        for (Map<String, String> data : productsData) {
            Product product = new Product();
            product.setName(data.get("name"));
            product.setDescription(data.get("description"));
            product.setPrice(Long.parseLong(data.get("price")));
            productRepository.save(product);
        }
    }

    @Y("la lista debería contener {int} productos")
    public void laListaDeberiaContenerProductos(int expectedCount) {
        List<Product> products = response.jsonPath().getList("", Product.class);
        assertEquals(expectedCount, products.size());
    }

    @Dado("que existe un producto con nombre {string}")
    public void queExisteUnProductoConNombre(String productName) {
        Product product = new Product();
        product.setName(productName);
        product.setDescription("Descripción de " + productName);
        product.setPrice(100L);
        product = productRepository.save(product);
        currentProductId = product.getId();
    }

    @Cuando("solicito los detalles del producto")
    public void solicitoLosDetallesDelProducto() {
        response = request.when().get("/products/" + currentProductId);
        if (response.statusCode() == 200) {
            currentProduct = response.as(Product.class);
        }
    }

    @Cuando("actualizo el producto con los siguientes datos:")
    public void actualizoElProductoConLosSiguientesDatos(Map<String, String> updateData) {
        Product productUpdate = new Product();
        productUpdate.setName(updateData.get("name"));
        productUpdate.setDescription(updateData.get("description"));
        productUpdate.setPrice(Long.parseLong(updateData.get("price")));

        response = request
                .body(productUpdate)
                .when()
                .put("/products/" + currentProductId);

        if (response.statusCode() == 201) {
            currentProduct = response.as(Product.class);
        }
    }

    @Y("el producto actualizado debería tener nombre {string}")
    public void elProductoActualizadoDeberiaTenerNombre(String expectedName) {
        assertEquals(expectedName, currentProduct.getName());
    }

    @Cuando("elimino el producto")
    public void eliminoElProducto() {
        response = request.when().delete("/products/" + currentProductId);
        if (response.statusCode() == 200) {
            currentProduct = response.as(Product.class);
        }
    }

    @Y("el producto eliminado debería tener nombre {string}")
    public void elProductoEliminadoDeberiaTenerNombre(String expectedName) {
        assertEquals(expectedName, currentProduct.getName());
    }

    @Y("el producto no debería existir en el sistema")
    public void elProductoNoDeberiaExistirEnElSistema() {
        assertFalse(productRepository.existsById(currentProductId));
    }

    @Cuando("solicito los detalles del producto con ID {long}")
    public void solicitoLosDetallesDelProductoConId(Long productId) {
        response = request.when().get("/products/" + productId);
    }

    @Cuando("intento actualizar el producto con ID {long}")
    public void intentoActualizarElProductoConId(Long productId) {
        Product productUpdate = new Product();
        productUpdate.setName("Producto Inexistente");
        productUpdate.setDescription("No debería actualizarse");
        productUpdate.setPrice(999L);

        response = request
                .body(productUpdate)
                .when()
                .put("/products/" + productId);
    }

    @Cuando("intento eliminar el producto con ID {long}")
    public void intentoEliminarElProductoConId(Long productId) {
        response = request.when().delete("/products/" + productId);
    }
}
