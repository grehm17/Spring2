package com.geekbrains.springbootproject.repositories;

        import com.geekbrains.springbootproject.entities.Category;
        import com.geekbrains.springbootproject.entities.Product;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.Pageable;
        import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
        import org.springframework.data.repository.PagingAndSortingRepository;
        import org.springframework.stereotype.Repository;

        import java.util.List;

@Repository
public interface CategoriesRepository extends PagingAndSortingRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    Page<Category> findAll(Pageable pageable);
    Category findOneByTitle(String title);
    List<Category> findAllByLevel(Long level);
    List<Category> findAllByParent(Category parent);
}
