package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

    // ===== CREATE =====

    @Test
    void create_shouldReturnSameProduct() {
        Product product = new Product();

        Product result = service.create(product);

        assertEquals(product, result, "Service should return created product");
    }

    @Test
    void create_shouldCallRepository() {
        Product product = new Product();

        service.create(product);

        Mockito.verify(repository).create(product);
    }

    // ===== FIND ALL =====

    @Test
    void findAll_shouldReturnTwoProducts() {
        Product p1 = new Product();
        Product p2 = new Product();

        Iterator<Product> iterator = List.of(p1, p2).iterator();
        Mockito.when(repository.findAll()).thenReturn(iterator);

        List<Product> result = service.findAll();

        assertEquals(2, result.size(), "Product list size should be 2");
    }

    @Test
    void findAll_shouldContainFirstProduct() {
        Product p1 = new Product();
        Product p2 = new Product();

        Iterator<Product> iterator = List.of(p1, p2).iterator();
        Mockito.when(repository.findAll()).thenReturn(iterator);

        List<Product> result = service.findAll();

        assertTrue(result.contains(p1), "Product list should contain first product");
    }

    @Test
    void findAll_shouldContainSecondProduct() {
        Product p1 = new Product();
        Product p2 = new Product();

        Iterator<Product> iterator = List.of(p1, p2).iterator();
        Mockito.when(repository.findAll()).thenReturn(iterator);

        List<Product> result = service.findAll();

        assertTrue(result.contains(p2), "Product list should contain second product");
    }

    // ===== FIND BY ID =====

    @Test
    void findById_shouldReturnCorrectProduct() {
        UUID id = UUID.randomUUID();
        Product product = new Product();

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(product));

        Product result = service.findById(id);

        assertEquals(product, result, "Returned product should match repository result");
    }

    // ===== UPDATE =====

    @Test
    void update_shouldCallRepositoryUpdate() {
        Product product = new Product();

        service.update(product);

        Mockito.verify(repository).update(product);
    }

    // ===== DELETE =====

    @Test
    void deleteById_shouldCallRepositoryDelete() {
        UUID id = UUID.randomUUID();

        service.deleteById(id);

        Mockito.verify(repository).deleteById(id);
    }
}