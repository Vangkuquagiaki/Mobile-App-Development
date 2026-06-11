package NguyenMinhThien.authapp.repository;

import NguyenMinhThien.authapp.entity.ProductSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import NguyenMinhThien.authapp.entity.ProductSupplierId;

@Repository
public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, ProductSupplierId> {
}
