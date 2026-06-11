package NguyenMinhThien.authapp.repository;

import NguyenMinhThien.authapp.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import NguyenMinhThien.authapp.entity.WishlistId;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, WishlistId> {
}
