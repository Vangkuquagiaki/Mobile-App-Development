package NguyenMinhThien.authapp.service.impl;

import NguyenMinhThien.authapp.entity.Order;
import NguyenMinhThien.authapp.repository.OrderRepository;
import NguyenMinhThien.authapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByCustomerUserId(userId);
    }
}
