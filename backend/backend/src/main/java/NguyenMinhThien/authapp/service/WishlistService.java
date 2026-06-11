package NguyenMinhThien.authapp.service;

import NguyenMinhThien.authapp.entity.Wishlist;
import NguyenMinhThien.authapp.entity.WishlistId;
import java.util.List;
import java.util.Optional;

public interface WishlistService {
    List<Wishlist> getAllWishlists();
    Optional<Wishlist> getWishlistById(WishlistId id);
    Wishlist saveWishlist(Wishlist wishlist);
    Wishlist updateWishlist(Wishlist wishlist);
    void deleteWishlist(WishlistId id);
}
