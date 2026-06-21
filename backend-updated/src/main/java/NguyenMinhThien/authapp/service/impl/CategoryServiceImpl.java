package NguyenMinhThien.authapp.service.impl;

import NguyenMinhThien.authapp.entity.Category;
import NguyenMinhThien.authapp.repository.CategoryRepository;
import NguyenMinhThien.authapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
