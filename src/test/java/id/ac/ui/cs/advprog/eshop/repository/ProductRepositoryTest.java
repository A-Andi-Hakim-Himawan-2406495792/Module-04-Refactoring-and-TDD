package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
    }

    @Test
    void create_shouldGenerateId() {
        Product product = new Product();
        product.setProductName("Sampo");
        product.setProductQuantity(1);

        productRepository.create(product);

        Product saved = productRepository.findAll().get(0);
        assertNotNull(saved.getProductId(), "Created product should have ID");
    }

    @Test
    void create_shouldSaveName() {
        Product product = new Product();
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        productRepository.create(product);

        Product saved = productRepository.findAll().get(0);
        assertEquals("Sampo Cap Bambang", saved.getProductName(), "Name should be saved");
    }

    @Test
    void create_shouldSaveQuantity() {
        Product product = new Product();
        product.setProductName("Sampo");
        product.setProductQuantity(100);

        productRepository.create(product);

        Product saved = productRepository.findAll().get(0);
        assertEquals(100, saved.getProductQuantity(), "Quantity should be saved");
    }

    @Test
    void findAll_emptyRepository() {
        assertTrue(productRepository.findAll().isEmpty(), "Empty repo should return no data");
    }

    @Test
    void update_existingProduct_shouldChangeName() {
        Product product = new Product();
        product.setProductName("Old");
        product.setProductQuantity(1);
        productRepository.create(product);

        UUID id = product.getProductId();

        Product updated = new Product();
        updated.setProductId(id);
        updated.setProductName("New");
        updated.setProductQuantity(1);

        productRepository.update(updated);

        assertEquals("New", productRepository.findById(id).orElseThrow().getProductName(), "Name should update");
    }

    @Test
    void delete_existingProduct_shouldRemove() {
        Product product = new Product();
        product.setProductName("Delete");
        product.setProductQuantity(1);
        productRepository.create(product);

        productRepository.deleteById(product.getProductId());

        assertTrue(productRepository.findAll().isEmpty(), "Product should be deleted");
    }

    @Test
    void findById_notFound_shouldReturnEmpty() {
        assertEquals(Optional.empty(), productRepository.findById(UUID.randomUUID()), "Unknown ID should return empty optional");
    }

    @Test
    void testFindByIdNotFound() {
        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setProductName("Sampo Cap Bambang");
        productRepository.create(product);
        Optional<Product> foundProduct = productRepository.findById(UUID.randomUUID());
        assertTrue(foundProduct.isEmpty(), "Product seharusnya Optional.empty jika UUID tidak ditemukan di repository");
    }

    @Test
    void testUpdateNotFound() {
        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setProductName("Sampo Cap Bambang");
        productRepository.create(product);

        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId(UUID.randomUUID());
        nonExistentProduct.setProductName("Produk Ilegal");

        productRepository.update(nonExistentProduct);
        Product originalProduct = productRepository.findById(product.getProductId()).orElseThrow();
        assertEquals("Sampo Cap Bambang", originalProduct.getProductName(), "Nama produk tidak boleh berubah jika mencoba update produk yang tidak ada");
    }
}


