package NguyenMinhThien.authapp.service.impl;

import NguyenMinhThien.authapp.entity.Coupon;
import NguyenMinhThien.authapp.repository.CouponRepository;
import NguyenMinhThien.authapp.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public Optional<Coupon> getCouponById(UUID id) {
        return couponRepository.findById(id);
    }

    @Override
    public Coupon saveCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public Coupon updateCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public void deleteCoupon(UUID id) {
        couponRepository.deleteById(id);
    }
}
