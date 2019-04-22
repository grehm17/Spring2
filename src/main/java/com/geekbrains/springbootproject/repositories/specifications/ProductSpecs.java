package com.geekbrains.springbootproject.repositories.specifications;

import com.geekbrains.springbootproject.entities.Category;
import com.geekbrains.springbootproject.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecs {
    public static Specification<Product> titleContains(String word) { // title LIKE 'apple%'
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + word + "%");
    }

    public static Specification<Product> priceGreaterThanOrEq(double value) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), value);
        };
    }

    public static Specification<Product> priceLesserThanOrEq(double value) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), value);
        };
    }

    public static Specification<Product> categoryEquals(Category category) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    public static Specification<Product> createSpec(String specType, String fieldName, String value){

        if (specType.equals("like")){
            return (Specification<Product>) (root,criterialQuery,criteriaBuilder) -> criteriaBuilder.like(root.get(fieldName), "%"+value+"%");
        }else if (specType.equals("GTOE")){
            return (Specification<Product>) (root,criterialQuery,criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), Double.parseDouble(value));
        }else if (specType.equals("LTOE")){
            return (Specification<Product>) (root,criterialQuery,criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), Double.parseDouble(value));
        }else if (specType.equals("eq")){
            return (Specification<Product>) (root,criterialQuery,criteriaBuilder) -> criteriaBuilder.equal(root.get(fieldName), Integer.parseInt(value));
        }else{
            return null;
        }
    }
}
