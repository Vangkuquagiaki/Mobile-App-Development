package NguyenMinhThien.authapp.service;

import NguyenMinhThien.authapp.entity.Order;
import java.util.List;

public interface OrderService {
    List<Order> getUserOrders(Long userId);
}
