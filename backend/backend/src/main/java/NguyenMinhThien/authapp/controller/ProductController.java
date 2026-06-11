package NguyenMinhThien.authapp.controller;

import NguyenMinhThien.authapp.dto.request.ProductRequest;
import NguyenMinhThien.authapp.dto.response.ProductDTO;
import NguyenMinhThien.authapp.entity.Product;
import NguyenMinhThien.authapp.mapper.ProductMapper;
import NguyenMinhThien.authapp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/sale")
    public ResponseEntity<List<ProductDTO>> getSaleProducts() {
        List<ProductDTO> products = productService.getSaleProducts().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/new")
    public ResponseEntity<List<ProductDTO>> getNewProducts() {
        List<ProductDTO> products = productService.getNewProducts().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/tag/{tagName}")
    public ResponseEntity<List<ProductDTO>> getProductsByTag(@PathVariable String tagName) {
        List<ProductDTO> products = productService.getProductsByTag(tagName).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.ok(productMapper.toDTO(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable UUID id, 
            @Valid @RequestBody ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(productMapper.toDTO(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable UUID categoryId) {
        List<ProductDTO> products = productService.getProductsByCategory(categoryId).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }
}
