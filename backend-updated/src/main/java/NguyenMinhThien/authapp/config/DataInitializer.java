package NguyenMinhThien.authapp.config;

import NguyenMinhThien.authapp.entity.Category;
import NguyenMinhThien.authapp.entity.Product;
import NguyenMinhThien.authapp.entity.Tag;
import NguyenMinhThien.authapp.repository.CategoryRepository;
import NguyenMinhThien.authapp.repository.ProductRepository;
import NguyenMinhThien.authapp.repository.TagRepository;
import NguyenMinhThien.authapp.repository.ReviewRepository;
import NguyenMinhThien.authapp.repository.OrderRepository;
import NguyenMinhThien.authapp.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@org.springframework.core.annotation.Order(1)
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

        private final ProductRepository productRepository;
        private final TagRepository tagRepository;
        private final CategoryRepository categoryRepository;
        private final ReviewRepository reviewRepository;
        private final OrderRepository orderRepository;
        private final OrderItemRepository orderItemRepository;

        @Override
        @Transactional
        public void run(String... args) throws Exception {
                // Ensure tags exist
                Tag tagNew = tagRepository.findByTagName("NEW")
                                .orElseGet(() -> tagRepository.save(Tag.builder().tagName("NEW").build()));

                Tag tagSale = tagRepository.findByTagName("SALE")
                                .orElseGet(() -> tagRepository.save(Tag.builder().tagName("SALE").build()));

                Tag tagTops = tagRepository.findByTagName("TOPS")
                                .orElseGet(() -> tagRepository.save(Tag.builder().tagName("TOPS").build()));

                // Clean up old products to ensure we re-seed with sizes and colors
                log.info("Resetting old reviews, order items, orders, and products for clean reseed...");
                reviewRepository.deleteAll();
                orderItemRepository.deleteAll();
                orderRepository.deleteAll();
                productRepository.deleteAll();

                List<Category> allCategories = categoryRepository.findAll();
                for (Category cat : allCategories) {
                        if (cat.getParent() != null) {
                                categoryRepository.delete(cat);
                        }
                }
                categoryRepository.flush();

                allCategories = categoryRepository.findAll();
                for (Category cat : allCategories) {
                        String name = cat.getCategoryName().toLowerCase();
                        if (!name.equals("new") && !name.equals("clothes") && !name.equals("shoes") && !name.equals("accessories")) {
                                categoryRepository.delete(cat);
                        }
                }
                categoryRepository.flush();

                // Ensure categories exist
                Category catNew = categoryRepository.findByCategoryNameIgnoreCase("New")
                                .orElseGet(() -> categoryRepository.save(Category.builder()
                                                .categoryName("New")
                                                .categoryDescription("New arrivals")
                                                .image("/images/cat_new.jpg")
                                                .active(true)
                                                .createdAt(java.time.OffsetDateTime.now())
                                                .updatedAt(java.time.OffsetDateTime.now())
                                                .build()));

                Category catClothes = categoryRepository.findByCategoryNameIgnoreCase("Clothes")
                                .orElseGet(() -> categoryRepository.save(Category.builder()
                                                .categoryName("Clothes")
                                                .categoryDescription("Clothes collection")
                                                .image("/images/cat_clothes.jpg")
                                                .active(true)
                                                .createdAt(java.time.OffsetDateTime.now())
                                                .updatedAt(java.time.OffsetDateTime.now())
                                                .build()));

                Category catShoes = categoryRepository.findByCategoryNameIgnoreCase("Shoes")
                                .orElseGet(() -> categoryRepository.save(Category.builder()
                                                .categoryName("Shoes")
                                                .categoryDescription("Shoes collection")
                                                .image("/images/cat_shoes.jpg")
                                                .active(true)
                                                .createdAt(java.time.OffsetDateTime.now())
                                                .updatedAt(java.time.OffsetDateTime.now())
                                                .build()));

                Category catAccessories = categoryRepository.findByCategoryNameIgnoreCase("Accessories")
                                .orElseGet(() -> categoryRepository.save(Category.builder()
                                                .categoryName("Accessories")
                                                .categoryDescription("Accessories collection")
                                                .image("/images/cat_accessories.jpg")
                                                .active(true)
                                                .createdAt(java.time.OffsetDateTime.now())
                                                .updatedAt(java.time.OffsetDateTime.now())
                                                .build()));

                // Create subcategories of Clothes
                List<String> subCategoriesNames = List.of(
                    "Tops", "Shirts & Blouses", "Cardigans & Sweaters", "Knitwear",
                    "Blazers", "Outerwear", "Pants", "Jeans", "Shorts", "Skirts", "Dresses"
                );
                for (String name : subCategoriesNames) {
                    if (categoryRepository.findByCategoryNameIgnoreCase(name).isEmpty()) {
                        categoryRepository.save(Category.builder()
                                .categoryName(name)
                                .parent(catClothes)
                                .active(true)
                                .createdAt(java.time.OffsetDateTime.now())
                                .updatedAt(java.time.OffsetDateTime.now())
                                .build());
                    }
                }
                categoryRepository.flush();

                // Initialize basic products
                log.info("Initializing basic product and tag data with sizes and colors...");
                Product p1 = Product.builder()
                                .productName("Evening Dress")
                                .brandName("Dorothy Perkins")
                                .slug("evening-dress")
                                .imageUrl("/images/product1.jpg")
                                .salePrice(12.0)
                                .comparePrice(15.0)
                                .quantity(50)
                                .shortDescription("Đầm dạ hội thanh lịch dáng hồng")
                                .productDescription("Đầm dạ hội phong cách thanh lịch, chất liệu mát mẻ phù hợp cho mùa hè.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(10)
                                .note("Red")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Red", "Black")))
                                .tags(new HashSet<>(Set.of(tagSale)))
                                .build();

                Product p2 = Product.builder()
                                .productName("Sport Dress")
                                .brandName("Sitlly")
                                .slug("sport-dress")
                                .imageUrl("/images/product2.jpg")
                                .salePrice(19.0)
                                .comparePrice(22.0)
                                .quantity(30)
                                .shortDescription("Đầm thể thao năng động co giãn")
                                .productDescription("Đầm thể thao dáng suông dài năng động, chất liệu cotton co giãn cao cấp.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(10)
                                .note("Navy")
                                .sizes(new HashSet<>(Set.of("S", "M", "L", "XL")))
                                .colors(new HashSet<>(Set.of("Navy", "Grey")))
                                .tags(new HashSet<>(Set.of(tagSale)))
                                .build();

                Product p3 = Product.builder()
                                .productName("Oversize T-Shirt")
                                .brandName("GUCCI")
                                .slug("oversize-tshirt-gucci")
                                .imageUrl("/images/product3.jpg")
                                .salePrice(650.0)
                                .comparePrice(0.0)
                                .quantity(15)
                                .shortDescription("Áo thun form rộng Gucci họa tiết cao cấp")
                                .productDescription("Áo thun form rộng cao cấp từ thương hiệu Gucci thời trang phong cách.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.5)
                                .reviewCount(8)
                                .note("Black")
                                .sizes(new HashSet<>(Set.of("M", "L", "XL")))
                                .colors(new HashSet<>(Set.of("Black", "White")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                p1 = productRepository.save(p1);
                p2 = productRepository.save(p2);
                p3 = productRepository.save(p3);

                linkProductToCategory(p1, catClothes);
                linkProductToCategory(p2, catClothes);
                linkProductToCategory(p3, catNew);

                // Initialize TOPS products
                log.info("Initializing TOPS product data with sizes and colors...");
                Product p4 = Product.builder()
                                .productName("T-Shirt SPANISH")
                                .brandName("Mango")
                                .slug("t-shirt-spanish-mango")
                                .imageUrl("/images/top1.jpg")
                                .salePrice(9.0)
                                .comparePrice(0.0)
                                .quantity(20)
                                .shortDescription("Áo thun T-Shirt SPANISH Mango")
                                .productDescription("Áo thun T-Shirt SPANISH Mango chất liệu cotton mềm mại thoải mái.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.0)
                                .reviewCount(3)
                                .note("Beige")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Black", "Beige", "Red")))
                                .tags(new HashSet<>(Set.of(tagTops, tagNew)))
                                .build();

                Product p5 = Product.builder()
                                .productName("Blouse")
                                .brandName("Dorothy Perkins")
                                .slug("blouse-dorothy-perkins")
                                .imageUrl("/images/top2.jpg")
                                .salePrice(14.0)
                                .comparePrice(21.0)
                                .quantity(15)
                                .shortDescription("Áo blouse Dororthy nữ tính thanh lịch")
                                .productDescription("Áo blouse Dorothy Perkins chất liệu voan nhẹ mềm mại nữ tính.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(10)
                                .note("White")
                                .sizes(new HashSet<>(Set.of("XS", "S", "M")))
                                .colors(new HashSet<>(Set.of("White", "Beige", "Navy")))
                                .tags(new HashSet<>(Set.of(tagTops, tagSale)))
                                .build();

                Product p6 = Product.builder()
                                .productName("Shirt")
                                .brandName("Mango")
                                .slug("shirt-mango")
                                .imageUrl("/images/top3.jpg")
                                .salePrice(9.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Áo sơ mi Mango cơ bản")
                                .productDescription("Áo sơ mi nữ Mango kiểu dáng basic dễ phối đồ.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(0.0)
                                .reviewCount(0)
                                .note("White")
                                .sizes(new HashSet<>(Set.of("S", "M", "L", "XL")))
                                .colors(new HashSet<>(Set.of("White", "Grey", "Black")))
                                .tags(new HashSet<>(Set.of(tagTops, tagNew)))
                                .build();

                Product p7 = Product.builder()
                                .productName("Light blouse")
                                .brandName("Dorothy Perkins")
                                .slug("light-blouse-dorothy-perkins")
                                .imageUrl("/images/top4.jpg")
                                .salePrice(14.0)
                                .comparePrice(21.0)
                                .quantity(25)
                                .shortDescription("Áo blouse nhẹ Dorothy Perkins")
                                .productDescription("Áo blouse mỏng nhẹ Dorothy Perkins phong cách nhẹ nhàng.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(10)
                                .note("Beige")
                                .sizes(new HashSet<>(Set.of("S", "M")))
                                .colors(new HashSet<>(Set.of("Beige", "Red", "Navy")))
                                .tags(new HashSet<>(Set.of(tagTops, tagSale)))
                                .build();

                Product p11 = Product.builder()
                                .productName("Yankees Baseball Hat")
                                .brandName("s.Oliver")
                                .slug("yankees-baseball-hat")
                                .imageUrl("/images/yankees_hat.webp")
                                .salePrice(150.0)
                                .comparePrice(0.0)
                                .quantity(8)
                                .shortDescription("Mũ lưỡi trai Yankees phong cách trẻ trung")
                                .productDescription("Mũ lưỡi trai thương hiệu New York Yankees thiết kế phối màu cá tính, năng động.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.7)
                                .reviewCount(14)
                                .note("Black")
                                .sizes(new HashSet<>(Set.of("One Size")))
                                .colors(new HashSet<>(Set.of("Gold", "Silver", "Black")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                // Seed products from /pic directory
                Product p14 = Product.builder()
                                .productName("Trendy Jacket")
                                .brandName("Mango")
                                .slug("trendy-jacket-mango")
                                .imageUrl("/images/image.png")
                                .salePrice(75.0)
                                .comparePrice(0.0)
                                .quantity(12)
                                .shortDescription("Áo khoác gió thời trang Mango")
                                .productDescription("Áo khoác gió Mango thiết kế cá tính, chống gió chống nước nhẹ.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.5)
                                .reviewCount(6)
                                .note("Black")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Black", "Beige")))
                                .tags(new HashSet<>(Set.of(tagTops, tagNew)))
                                .build();

                Product p15 = Product.builder()
                                .productName("Casual Hoodie")
                                .brandName("adidas Originals")
                                .slug("casual-hoodie-adidas")
                                .imageUrl("/images/image (1).png")
                                .salePrice(65.0)
                                .comparePrice(0.0)
                                .quantity(20)
                                .shortDescription("Áo nỉ Hoodie adidas Originals")
                                .productDescription("Áo nỉ có mũ adidas Originals mềm mại phong cách năng động thể thao.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.7)
                                .reviewCount(18)
                                .note("Gray")
                                .sizes(new HashSet<>(Set.of("M", "L", "XL")))
                                .colors(new HashSet<>(Set.of("Gray", "Black", "Red")))
                                .tags(new HashSet<>(Set.of(tagTops, tagNew)))
                                .build();

                Product p16 = Product.builder()
                                .productName("Active Wear")
                                .brandName("adidas")
                                .slug("active-wear-adidas")
                                .imageUrl("/images/image (2).png")
                                .salePrice(40.0)
                                .comparePrice(60.0)
                                .quantity(30)
                                .shortDescription("Bộ đồ thể thao adidas năng động")
                                .productDescription("Bộ quần áo thể thao adidas thoáng mát phù hợp luyện tập.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.3)
                                .reviewCount(8)
                                .note("Black")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Black", "White")))
                                .tags(new HashSet<>(Set.of(tagTops, tagSale)))
                                .build();

                Product p17 = Product.builder()
                                .productName("Denim Jacket")
                                .brandName("Jack & Jones")
                                .slug("denim-jacket-jack-jones")
                                .imageUrl("/images/image (3).png")
                                .salePrice(89.0)
                                .comparePrice(0.0)
                                .quantity(15)
                                .shortDescription("Áo khoác bò Denim Jack & Jones")
                                .productDescription("Áo khoác Denim bò nam từ thương hiệu Jack & Jones phong cách bụi bặm.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.8)
                                .reviewCount(10)
                                .note("Blue")
                                .sizes(new HashSet<>(Set.of("M", "L", "XL")))
                                .colors(new HashSet<>(Set.of("Blue", "Grey")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p18 = Product.builder()
                                .productName("Warm Parka")
                                .brandName("Blend")
                                .slug("warm-parka-blend")
                                .imageUrl("/images/image (4).png")
                                .salePrice(110.0)
                                .comparePrice(160.0)
                                .quantity(15)
                                .shortDescription("Áo khoác ấm phao Parka Blend")
                                .productDescription("Áo khoác giữ ấm chống tuyết Parka Blend phong cách Bắc Âu ấm áp.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.6)
                                .reviewCount(22)
                                .note("Navy")
                                .sizes(new HashSet<>(Set.of("S", "M", "L", "XL")))
                                .colors(new HashSet<>(Set.of("Navy", "Black")))
                                .tags(new HashSet<>(Set.of(tagSale)))
                                .build();

                Product p19 = Product.builder()
                                .productName("Elegant Suit")
                                .brandName("Boutique Moschino")
                                .slug("elegant-suit-moschino")
                                .imageUrl("/images/image (5).png")
                                .salePrice(450.0)
                                .comparePrice(0.0)
                                .quantity(5)
                                .shortDescription("Bộ Suit Moschino sang trọng quý phái")
                                .productDescription("Bộ Comple Suit thiết kế cao cấp từ thương hiệu Boutique Moschino thanh lịch.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(2)
                                .note("Black")
                                .sizes(new HashSet<>(Set.of("XS", "S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Black", "White", "Beige")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p20 = Product.builder()
                                .productName("Winter Coat")
                                .brandName("Champion")
                                .slug("winter-coat-champion")
                                .imageUrl("/images/image (6).png")
                                .salePrice(125.0)
                                .comparePrice(0.0)
                                .quantity(18)
                                .shortDescription("Áo khoác phao dày ấm Champion")
                                .productDescription("Áo phao dày mùa đông Champion giữ ấm tối ưu phom dáng thể thao.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.4)
                                .reviewCount(14)
                                .note("Black")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Black", "Grey")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p21 = Product.builder()
                                .productName("Fashion Cardigan")
                                .brandName("s.Oliver")
                                .slug("fashion-cardigan-soliver")
                                .imageUrl("/images/image (7).png")
                                .salePrice(45.0)
                                .comparePrice(65.0)
                                .quantity(25)
                                .shortDescription("Áo len mỏng Cardigan s.Oliver")
                                .productDescription("Áo len cài khuy mỏng nhẹ Cardigan s.Oliver trẻ trung năng động.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.6)
                                .reviewCount(9)
                                .note("Beige")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Beige", "White")))
                                .tags(new HashSet<>(Set.of(tagSale)))
                                .build();

                Product p22 = Product.builder()
                                .productName("Slim Fit Jeans")
                                .brandName("Diesel")
                                .slug("slim-fit-jeans-diesel")
                                .imageUrl("/images/image (8).png")
                                .salePrice(110.0)
                                .comparePrice(0.0)
                                .quantity(35)
                                .shortDescription("Quần Jeans bò Diesel dáng ôm")
                                .productDescription("Quần bò nam Jeans dáng ôm Slim Fit co giãn thoải mái hiệu Diesel.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.7)
                                .reviewCount(17)
                                .note("Blue")
                                .sizes(new HashSet<>(Set.of("30", "31", "32", "33")))
                                .colors(new HashSet<>(Set.of("Blue", "Black")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p23 = Product.builder()
                                .productName("Leather Handbag")
                                .brandName("Red Valentino")
                                .slug("leather-handbag-valentino")
                                .imageUrl("/images/image (9).png")
                                .salePrice(290.0)
                                .comparePrice(0.0)
                                .quantity(8)
                                .shortDescription("Túi xách da cao cấp Red Valentino")
                                .productDescription("Túi xách nữ chất liệu da cao cấp chính hãng Red Valentino sang xịn mịn.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(3)
                                .note("Red")
                                .sizes(new HashSet<>(Set.of("One Size")))
                                .colors(new HashSet<>(Set.of("Red", "Black", "Beige")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p24 = Product.builder()
                                .productName("Elegant Summer Dress")
                                .brandName("Mango")
                                .slug("elegant-summer-dress")
                                .imageUrl("/images/summer_dress.png")
                                .salePrice(95.0)
                                .comparePrice(0.0)
                                .quantity(12)
                                .shortDescription("Đầm hè phối ren hoa ngọt ngào")
                                .productDescription("Đầm hè phong cách trẻ trung phối ren hoa ngọt ngào hiệu Mango.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.9)
                                .reviewCount(19)
                                .note("White")
                                .sizes(new HashSet<>(Set.of("XS", "S", "M")))
                                .colors(new HashSet<>(Set.of("White", "Red", "Beige")))
                                .tags(new HashSet<>(Set.of(tagTops, tagNew)))
                                .build();

                Product p25 = Product.builder()
                                .productName("Sport T-Shirt")
                                .brandName("adidas")
                                .slug("sport-tshirt-adidas")
                                .imageUrl("/images/z7888710050382_c815de657ca1f5b7ddcbf867874e8f03.jpg")
                                .salePrice(29.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Áo thun thể thao adidas thoáng mát")
                                .productDescription("Áo thun thể thao adidas chất liệu thoáng khí, co giãn cực tốt.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Black", "Red")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p26 = Product.builder()
                                .productName("Stylish Blouse")
                                .brandName("Mango")
                                .slug("stylish-blouse-mango")
                                .imageUrl("/images/z7888710132105_a4427b5a67d19b57addc1da878907bb5.jpg")
                                .salePrice(39.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Áo blouse nữ Mango thời trang thanh lịch")
                                .productDescription("Áo blouse Mango dáng ôm thanh lịch thích hợp cho môi trường công sở.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("XS", "S", "M")))
                                .colors(new HashSet<>(Set.of("White", "Beige")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p27 = Product.builder()
                                .productName("Cozy Knitwear")
                                .brandName("s.Oliver")
                                .slug("cozy-knitwear-soliver")
                                .imageUrl("/images/z7888714530900_1a586e868ae3ca7b027c8a45b41c0d1b.jpg")
                                .salePrice(69.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Áo len cardigan s.Oliver ấm áp")
                                .productDescription("Áo len cardigan dệt kim s.Oliver mềm mại, giữ nhiệt tốt cho mùa đông lạnh.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("S", "M", "L", "XL")))
                                .colors(new HashSet<>(Set.of("Beige", "Gray")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p28 = Product.builder()
                                .productName("Casual Suit")
                                .brandName("Boutique Moschino")
                                .slug("casual-suit-moschino")
                                .imageUrl("/images/z7888714623412_9757291a319e139f66e190e235c71734.jpg")
                                .salePrice(199.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Bộ suit casual Moschino thanh lịch")
                                .productDescription("Bộ suit casual Boutique Moschino phom dáng hiện đại sang trọng.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Black", "Gray")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p29 = Product.builder()
                                .productName("Knit Pullover")
                                .brandName("Dorothy Perkins")
                                .slug("knit-pullover-dorothy")
                                .imageUrl("/images/z7889472051071_732f5bad4ebafe1b181a790c05127cd2.jpg")
                                .salePrice(49.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Áo len cổ tròn Dorothy Perkins nhẹ nhàng")
                                .productDescription("Áo len cổ tròn dệt kim Dorothy Perkins mỏng nhẹ, giữ ấm vừa phải.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Gray", "Blue")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p30 = Product.builder()
                                .productName("Summer Crop Top")
                                .brandName("Mango")
                                .slug("summer-crop-top-mango")
                                .imageUrl("/images/z7889472114938_990a1e9bb7b8741d00593e620a50ab96.jpg")
                                .salePrice(19.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Áo thun crop top Mango mát mẻ")
                                .productDescription("Áo thun crop top Mango năng động trẻ trung chất liệu cotton mát mẻ.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("XS", "S", "M")))
                                .colors(new HashSet<>(Set.of("White", "Black")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p31 = Product.builder()
                                .productName("Luxury Handbag")
                                .brandName("GUCCI")
                                .slug("luxury-handbag-gucci")
                                .imageUrl("/images/z7896061153845_9b96b466a48f9b69b37bef5bba366cd6.jpg")
                                .salePrice(1200.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Túi xách da GUCCI sang trọng quý phái")
                                .productDescription("Túi xách da cao cấp từ thương hiệu xa xỉ GUCCI, thiết kế đẳng cấp thượng lưu.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("One Size")))
                                .colors(new HashSet<>(Set.of("Black", "Beige")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p32 = Product.builder()
                                .productName("Designer Shirt")
                                .brandName("GUCCI")
                                .slug("designer-shirt-gucci")
                                .imageUrl("/images/z7896061153897_a1affd7cafd0e1981adaca17c1a00f05.jpg")
                                .salePrice(450.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Áo sơ mi Gucci họa tiết độc đáo")
                                .productDescription("Áo sơ mi lụa Gucci in họa tiết đặc trưng nổi bật và cá tính thời thượng.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("S", "M", "L", "XL")))
                                .colors(new HashSet<>(Set.of("White", "Black")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p33 = Product.builder()
                                .productName("Spring Jacket")
                                .brandName("Jack & Jones")
                                .slug("spring-jacket-jj")
                                .imageUrl("/images/z7896061215982_3e3ff4b587a384f8f26741ae8971bdd5.jpg")
                                .salePrice(89.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Áo khoác nhẹ Jack & Jones cho mùa xuân")
                                .productDescription("Áo khoác nhẹ Jack & Jones phong cách trẻ trung năng động cho ngày xuân mát mẻ.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("M", "L", "XL")))
                                .colors(new HashSet<>(Set.of("Black", "Blue")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p34 = Product.builder()
                                .productName("Cozy Sweater")
                                .brandName("Blend")
                                .slug("cozy-sweater-blend")
                                .imageUrl("/images/z7896061244018_33d1f0a4cc39f828e69a0ff3725f7383.jpg")
                                .salePrice(55.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Áo nỉ Blend ấm áp giữ nhiệt tốt")
                                .productDescription("Áo nỉ dài tay chui đầu hiệu Blend chất liệu cotton nỉ bông siêu ấm áp.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("S", "M", "L", "XL")))
                                .colors(new HashSet<>(Set.of("Gray", "Blue")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p35 = Product.builder()
                                .productName("Winter Parka")
                                .brandName("Champion")
                                .slug("winter-parka-champion")
                                .imageUrl("/images/z7896061272915_47399beb55c4afe59a7ae310a066eefe.jpg")
                                .salePrice(150.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Áo phao mùa đông Champion dày ấm")
                                .productDescription("Áo khoác phao dày chống gió rét hiệu Champion phù hợp du lịch trượt tuyết.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("M", "L", "XL")))
                                .colors(new HashSet<>(Set.of("Black", "Gray")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p36 = Product.builder()
                                .productName("Blue Casual Collar Long Sleeve")
                                .brandName("Mango")
                                .slug("blue-casual-collar-long-sleeve")
                                .imageUrl("/images/blue_casual_collar.webp")
                                .salePrice(45.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Áo sơ mi dài tay cổ bẻ phong cách Casual trẻ trung, năng động.")
                                .productDescription("Áo sơ mi dài tay Mango chất liệu vải dệt cao cấp, mềm mại và thoải mái.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Blue", "White")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p47 = Product.builder()
                                .productName("Vintage Leather Jacket")
                                .brandName("Zara")
                                .slug("vintage-leather-jacket-zara")
                                .imageUrl("/images/image.png")
                                .salePrice(120.0)
                                .comparePrice(180.0)
                                .quantity(30)
                                .shortDescription("Áo khoác da cổ điển sang trọng và nam tính.")
                                .productDescription("Áo khoác da Zara cổ điển chất liệu da thật cao cấp, giữ ấm tốt và tôn lên vẻ nam tính.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("M", "L", "XL")))
                                .colors(new HashSet<>(Set.of("Black", "Brown")))
                                .tags(new HashSet<>(Set.of(tagSale)))
                                .build();

                Product p48 = Product.builder()
                                .productName("Classic Denim Jeans")
                                .brandName("Levi's")
                                .slug("classic-denim-jeans-levis")
                                .imageUrl("/images/image (8).png")
                                .salePrice(65.0)
                                .comparePrice(0.0)
                                .quantity(40)
                                .shortDescription("Quần Jeans bò Levi's cổ điển.")
                                .productDescription("Quần Jeans Levi's chất liệu denim cao cấp bền bỉ, phom dáng cổ điển dễ dàng phối đồ.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("30", "31", "32", "33")))
                                .colors(new HashSet<>(Set.of("Blue", "Black")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p50 = Product.builder()
                                .productName("Casual Canvas Backpack")
                                .brandName("Herschel")
                                .slug("casual-canvas-backpack-herschel")
                                .imageUrl("/images/cat_accessories.jpg")
                                .salePrice(45.0)
                                .comparePrice(0.0)
                                .quantity(50)
                                .shortDescription("Balo vải canvas phong cách năng động hàng ngày.")
                                .productDescription("Balo Herschel chất liệu vải canvas chắc chắn, nhiều ngăn tiện lợi phù hợp đi học hay đi làm.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(5.0)
                                .reviewCount(0)
                                .sizes(new HashSet<>(Set.of("One Size")))
                                .colors(new HashSet<>(Set.of("Beige", "Black", "Navy")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p51 = Product.builder()
                                .productName("Ruffle Tiered Skirt Set")
                                .brandName("ZARA")
                                .slug("ruffle-tiered-skirt-set")
                                .imageUrl("/images/z7956025974169_fd438c145f87f0be97401125463378d1.jpg")
                                .salePrice(42.0)
                                .comparePrice(58.0)
                                .quantity(20)
                                .shortDescription("Set áo cột dây và chân váy xếp tầng bồng xinh xắn")
                                .productDescription("Set áo thun cột dây phối chân váy xếp tầng nhiều lớp, chất liệu cotton thoáng mát, phong cách trẻ trung năng động.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.8)
                                .reviewCount(6)
                                .note("White")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("White")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p52 = Product.builder()
                                .productName("Cherry Print Cami Dress")
                                .brandName("Mango")
                                .slug("cherry-print-cami-dress")
                                .imageUrl("/images/z7956025974270_a272210775b4517ea262f8246e7d9c29.jpg")
                                .salePrice(48.0)
                                .comparePrice(65.0)
                                .quantity(25)
                                .shortDescription("Đầm hai dây in họa tiết cherry ngọt ngào")
                                .productDescription("Đầm xòe hai dây cột nơ in họa tiết trái cherry, chất liệu cotton mềm mại, phù hợp dạo phố mùa hè.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.9)
                                .reviewCount(8)
                                .note("Cream")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Cream", "Red")))
                                .tags(new HashSet<>(Set.of(tagNew, tagSale)))
                                .build();

                Product p53 = Product.builder()
                                .productName("Wine Red Cami Dress")
                                .brandName("Mango")
                                .slug("wine-red-cami-dress")
                                .imageUrl("/images/z7956026033951_09ce793b0e63127a3600de08192c844b.jpg")
                                .salePrice(45.0)
                                .comparePrice(0.0)
                                .quantity(25)
                                .shortDescription("Đầm hai dây xòe màu đỏ rượu vang sang trọng")
                                .productDescription("Đầm hai dây cột nơ xòe bồng, màu đỏ rượu vang nổi bật, thiết kế tối giản dễ phối đồ cho nhiều dịp.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.7)
                                .reviewCount(5)
                                .note("Wine Red")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Wine Red")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p54 = Product.builder()
                                .productName("Lace-up Ruffle Mini Skirt Set")
                                .brandName("Urban Outfitters")
                                .slug("lace-up-ruffle-mini-skirt-set")
                                .imageUrl("/images/z7956026097460_5c8c93d24ad4afef62a66779b3b9db7a.jpg")
                                .salePrice(52.0)
                                .comparePrice(0.0)
                                .quantity(15)
                                .shortDescription("Set áo cổ lọ tay dài và chân váy xếp tầng phong cách cá tính")
                                .productDescription("Set áo croptop cổ lọ tay dài phối chân váy ngắn xếp tầng dây buộc cạnh, phong cách thời trang cá tính, năng động.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.6)
                                .reviewCount(4)
                                .note("Black")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Black")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p55 = Product.builder()
                                .productName("Bomber Jacket & Skirt Set")
                                .brandName("Mango")
                                .slug("bomber-jacket-skirt-set")
                                .imageUrl("/images/z7956026152712_f12ba4a0545a6bac9701abec172f8b89.jpg")
                                .salePrice(68.0)
                                .comparePrice(85.0)
                                .quantity(18)
                                .shortDescription("Set áo khoác bomber và chân váy ngắn phong cách Hàn Quốc")
                                .productDescription("Set áo khoác bomber phối cùng croptop và chân váy chữ A túi hộp, phong cách Hàn Quốc thanh lịch, dễ phối đồ đi làm hoặc dạo phố.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.8)
                                .reviewCount(7)
                                .note("Khaki")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Khaki", "Brown")))
                                .tags(new HashSet<>(Set.of(tagNew, tagSale)))
                                .build();

                Product p56 = Product.builder()
                                .productName("Off-Shoulder Ruffle Romper")
                                .brandName("PrettyLittleThing")
                                .slug("off-shoulder-ruffle-romper")
                                .imageUrl("/images/z7956026216286_b038e2819f9eca419cd361f7c1cc37a2.jpg")
                                .salePrice(39.0)
                                .comparePrice(0.0)
                                .quantity(20)
                                .shortDescription("Jumpsuit trễ vai bèo nhún quyến rũ")
                                .productDescription("Jumpsuit trễ vai phối bèo nhún điệu đà, chất liệu co giãn nhẹ, phù hợp dạo phố và dự tiệc.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.5)
                                .reviewCount(3)
                                .note("Black")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Black")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p57 = Product.builder()
                                .productName("Puff Sleeve Blouse & Skirt Set")
                                .brandName("ZARA")
                                .slug("puff-sleeve-blouse-skirt-set")
                                .imageUrl("/images/z7956026216382_4e6cb44ff1939b917e1a66c3c19485ff.jpg")
                                .salePrice(46.0)
                                .comparePrice(0.0)
                                .quantity(20)
                                .shortDescription("Set áo phồng tay và chân váy bó ngắn thanh lịch")
                                .productDescription("Set áo sơ mi trễ vai tay phồng phối chân váy bó ngắn, thiết kế thanh lịch, tôn dáng cho các buổi hẹn hò hoặc dạo phố.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.7)
                                .reviewCount(5)
                                .note("White")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("White", "Navy")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p58 = Product.builder()
                                .productName("Fluffy Bucket Hat")
                                .brandName("H&M")
                                .slug("fluffy-bucket-hat")
                                .imageUrl("/images/z7956026315301_e2e4d9632c6e3c5874937e09199e96c3.jpg")
                                .salePrice(22.0)
                                .comparePrice(0.0)
                                .quantity(40)
                                .shortDescription("Mũ bucket lông xù ấm áp phong cách Hàn Quốc")
                                .productDescription("Mũ bucket chất liệu lông xù mềm mại, giữ ấm tốt, phong cách Hàn Quốc dễ phối cùng nhiều trang phục mùa đông.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.6)
                                .reviewCount(9)
                                .note("White")
                                .sizes(new HashSet<>(Set.of("One Size")))
                                .colors(new HashSet<>(Set.of("White", "Beige")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p59 = Product.builder()
                                .productName("Graphic Tee & Denim Maxi Skirt Set")
                                .brandName("Mango")
                                .slug("graphic-tee-denim-maxi-skirt-set")
                                .imageUrl("/images/z7956026349660_2dbe13c7932127bba625715d6b753d42.jpg")
                                .salePrice(55.0)
                                .comparePrice(0.0)
                                .quantity(20)
                                .shortDescription("Set áo thun in hình và chân váy denim dài năng động")
                                .productDescription("Set áo thun in họa tiết phối chân váy denim dài xẻ tà, phong cách casual năng động, dễ kết hợp phụ kiện.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.7)
                                .reviewCount(6)
                                .note("Beige")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Beige", "Blue")))
                                .tags(new HashSet<>(Set.of(tagNew, tagTops)))
                                .build();

                Product p60 = Product.builder()
                                .productName("Bear Ear Hooded Jacket")
                                .brandName("Uniqlo")
                                .slug("bear-ear-hooded-jacket")
                                .imageUrl("/images/z7956054379621_ff139bcfe06cfe11c9dfa35e70ef8610.jpg")
                                .salePrice(58.0)
                                .comparePrice(75.0)
                                .quantity(22)
                                .shortDescription("Áo khoác mũ tai gấu dễ thương phong cách Nhật Bản")
                                .productDescription("Áo khoác có mũ thiết kế tai gấu đáng yêu, hai túi hộp tiện lợi, chất liệu chống gió nhẹ, phong cách Nhật Bản ấm áp.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.8)
                                .reviewCount(11)
                                .note("Beige")
                                .sizes(new HashSet<>(Set.of("S", "M", "L", "XL")))
                                .colors(new HashSet<>(Set.of("Beige")))
                                .tags(new HashSet<>(Set.of(tagNew, tagSale)))
                                .build();

                Product p61 = Product.builder()
                                .productName("Classic Navy Necktie")
                                .brandName("CAVAT")
                                .slug("classic-navy-necktie")
                                .imageUrl("/images/z7956054379622_66729b2a53cb57efe58e9199486b5527.jpg")
                                .salePrice(15.0)
                                .comparePrice(0.0)
                                .quantity(60)
                                .shortDescription("Cà vạt nam màu xanh navy lịch lãm")
                                .productDescription("Cà vạt nam chất liệu vải mềm mịn, màu xanh navy trang nhã, phù hợp đi làm hoặc dự tiệc trang trọng.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.5)
                                .reviewCount(2)
                                .note("Navy")
                                .sizes(new HashSet<>(Set.of("One Size")))
                                .colors(new HashSet<>(Set.of("Navy")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p62 = Product.builder()
                                .productName("Classic Black Necktie")
                                .brandName("CAVAT")
                                .slug("classic-black-necktie")
                                .imageUrl("/images/z7956054413977_31695a71aacb6151b2e617bd1feb17fb.jpg")
                                .salePrice(15.0)
                                .comparePrice(0.0)
                                .quantity(60)
                                .shortDescription("Cà vạt nam màu đen trang trọng")
                                .productDescription("Cà vạt nam màu đen basic dễ phối, chất liệu vải bóng nhẹ, phù hợp cho công sở và các sự kiện trang trọng.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.5)
                                .reviewCount(2)
                                .note("Black")
                                .sizes(new HashSet<>(Set.of("One Size")))
                                .colors(new HashSet<>(Set.of("Black")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p63 = Product.builder()
                                .productName("Tweed Crop Jacket")
                                .brandName("ZARA")
                                .slug("tweed-crop-jacket")
                                .imageUrl("/images/z7956054479360_5f747734437457a5d77babf42e8b741e.jpg")
                                .salePrice(89.0)
                                .comparePrice(120.0)
                                .quantity(15)
                                .shortDescription("Áo khoác tweed ngắn sang trọng cúc vàng")
                                .productDescription("Áo khoác tweed dáng ngắn cúc vàng nổi bật, chất liệu vải tweed cao cấp, phong cách thanh lịch quý phái.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.9)
                                .reviewCount(10)
                                .note("Cream")
                                .sizes(new HashSet<>(Set.of("S", "M", "L")))
                                .colors(new HashSet<>(Set.of("Cream")))
                                .tags(new HashSet<>(Set.of(tagNew, tagSale)))
                                .build();

                Product p64 = Product.builder()
                                .productName("Chunky Platform Oxford Shoes")
                                .brandName("Dr. Martens")
                                .slug("chunky-platform-oxford-shoes")
                                .imageUrl("/images/z7956054552012_93074e32c27c76fa98fe2dadab6ef836.jpg")
                                .salePrice(95.0)
                                .comparePrice(0.0)
                                .quantity(25)
                                .shortDescription("Giày oxford đế độn phong cách cá tính")
                                .productDescription("Giày oxford buộc dây đế độn dày dặn, thiết kế cá tính dễ phối đồ, có sẵn hai màu đen và trắng.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.7)
                                .reviewCount(8)
                                .note("Black")
                                .sizes(new HashSet<>(Set.of("36", "37", "38", "39", "40")))
                                .colors(new HashSet<>(Set.of("Black", "White")))
                                .tags(new HashSet<>(Set.of(tagNew)))
                                .build();

                Product p65 = Product.builder()
                                .productName("Platform Chunky Sneakers")
                                .brandName("New Balance")
                                .slug("platform-chunky-sneakers")
                                .imageUrl("/images/z7956054552115_6a2608670a68db32ebd984c2473e2a30.jpg")
                                .salePrice(78.0)
                                .comparePrice(99.0)
                                .quantity(30)
                                .shortDescription("Giày sneaker trắng đế độn năng động")
                                .productDescription("Giày sneaker đế độn cao cá tính, phối dây buộc nổi bật, chất liệu da tổng hợp bền đẹp, phù hợp mọi outfit năng động.")
                                .productType("simple")
                                .published(true)
                                .ratingAverage(4.8)
                                .reviewCount(13)
                                .note("White")
                                .sizes(new HashSet<>(Set.of("36", "37", "38", "39", "40")))
                                .colors(new HashSet<>(Set.of("White")))
                                .tags(new HashSet<>(Set.of(tagNew, tagSale)))
                                .build();

                p4 = productRepository.save(p4);
                p5 = productRepository.save(p5);
                p6 = productRepository.save(p6);
                p7 = productRepository.save(p7);
                p11 = productRepository.save(p11);
                p14 = productRepository.save(p14);
                p15 = productRepository.save(p15);
                p16 = productRepository.save(p16);
                p17 = productRepository.save(p17);
                p18 = productRepository.save(p18);
                p19 = productRepository.save(p19);
                p20 = productRepository.save(p20);
                p21 = productRepository.save(p21);
                p22 = productRepository.save(p22);
                p23 = productRepository.save(p23);
                p24 = productRepository.save(p24);
                p25 = productRepository.save(p25);
                p26 = productRepository.save(p26);
                p27 = productRepository.save(p27);
                p28 = productRepository.save(p28);
                p29 = productRepository.save(p29);
                p30 = productRepository.save(p30);
                p31 = productRepository.save(p31);
                p32 = productRepository.save(p32);
                p33 = productRepository.save(p33);
                p34 = productRepository.save(p34);
                p35 = productRepository.save(p35);
                p36 = productRepository.save(p36);
                p47 = productRepository.save(p47);
                p48 = productRepository.save(p48);
                p50 = productRepository.save(p50);
                p51 = productRepository.save(p51);
                p52 = productRepository.save(p52);
                p53 = productRepository.save(p53);
                p54 = productRepository.save(p54);
                p55 = productRepository.save(p55);
                p56 = productRepository.save(p56);
                p57 = productRepository.save(p57);
                p58 = productRepository.save(p58);
                p59 = productRepository.save(p59);
                p60 = productRepository.save(p60);
                p61 = productRepository.save(p61);
                p62 = productRepository.save(p62);
                p63 = productRepository.save(p63);
                p64 = productRepository.save(p64);
                p65 = productRepository.save(p65);

                Category catOuterwear = categoryRepository.findByCategoryNameIgnoreCase("Outerwear").orElse(catClothes);
                Category catJeans = categoryRepository.findByCategoryNameIgnoreCase("Jeans").orElse(catClothes);
                Category catSkirts = categoryRepository.findByCategoryNameIgnoreCase("Skirts").orElse(catClothes);
                Category catDresses = categoryRepository.findByCategoryNameIgnoreCase("Dresses").orElse(catClothes);

                linkProductToCategory(p4, catNew);
                linkProductToCategory(p5, catClothes);
                linkProductToCategory(p6, catClothes);
                linkProductToCategory(p7, catClothes);
                linkProductToCategory(p11, catAccessories);
                linkProductToCategory(p14, catClothes);
                linkProductToCategory(p15, catClothes);
                linkProductToCategory(p16, catClothes);
                linkProductToCategory(p17, catClothes);
                linkProductToCategory(p18, catClothes);
                linkProductToCategory(p19, catClothes);
                linkProductToCategory(p20, catClothes);
                linkProductToCategory(p21, catClothes);
                linkProductToCategory(p22, catClothes);
                linkProductToCategory(p23, catAccessories);
                linkProductToCategory(p24, catClothes);
                linkProductToCategory(p25, catClothes, catNew);
                linkProductToCategory(p26, catClothes, catNew);
                linkProductToCategory(p27, catClothes, catNew);
                linkProductToCategory(p28, catClothes, catNew);
                linkProductToCategory(p29, catClothes, catNew);
                linkProductToCategory(p30, catClothes, catNew);
                linkProductToCategory(p31, catAccessories, catNew);
                linkProductToCategory(p32, catClothes, catNew);
                linkProductToCategory(p33, catClothes, catNew);
                linkProductToCategory(p34, catClothes, catNew);
                linkProductToCategory(p35, catClothes, catNew);
                linkProductToCategory(p36, catClothes, catNew);
                linkProductToCategory(p47, catClothes, catOuterwear);
                linkProductToCategory(p48, catClothes, catJeans, catNew);
                linkProductToCategory(p50, catAccessories, catNew);
                linkProductToCategory(p51, catClothes, catSkirts, catNew);
                linkProductToCategory(p52, catClothes, catDresses, catNew);
                linkProductToCategory(p53, catClothes, catDresses, catNew);
                linkProductToCategory(p54, catClothes, catSkirts, catNew);
                linkProductToCategory(p55, catClothes, catSkirts, catNew);
                linkProductToCategory(p56, catClothes, catDresses, catNew);
                linkProductToCategory(p57, catClothes, catSkirts, catNew);
                linkProductToCategory(p58, catAccessories, catNew);
                linkProductToCategory(p59, catClothes, catSkirts, catNew);
                linkProductToCategory(p60, catClothes, catOuterwear, catNew);
                linkProductToCategory(p61, catAccessories, catNew);
                linkProductToCategory(p62, catAccessories, catNew);
                linkProductToCategory(p63, catClothes, catOuterwear, catNew);
                linkProductToCategory(p64, catShoes, catNew);
                linkProductToCategory(p65, catShoes, catNew);
                log.info("TOPS and other product data initialized successfully.");

        }

        private void linkProductToCategory(Product product, Category... categories) {
                if (product.getCategories() == null) {
                        product.setCategories(new HashSet<>());
                }
                boolean modified = false;
                for (Category category : categories) {
                        boolean exists = product.getCategories().stream()
                                .anyMatch(c -> c.getId().equals(category.getId()));
                        if (!exists) {
                                product.getCategories().add(category);
                                modified = true;
                        }
                }
                if (modified) {
                        productRepository.save(product);
                }
        }
}