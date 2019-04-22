package com.geekbrains.springbootproject.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "level")
    private Long level;

    public void setParent(Category parent) {
        this.parent = parent;
        this.level = parent.getLevel()+1;
    }

    @ManyToOne
    @JoinColumn(name = "parent")
    private Category parent;

    @ManyToMany
    @JoinTable(
            name = "category_property",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "property_id")
    )
    private List<Property> properties;

    public List<Category> getHierarchy(List<Category> hrList){
        if(parent != null){
            hrList = parent.getHierarchy(hrList);
        }
        hrList.add(this);
        return hrList;
    }

    public List<Property> getAllProperties(List<Property> prList){
        if(parent != null){
            prList = parent.getAllProperties(prList);
        }
        prList.addAll(this.getProperties());
        return prList;
    }
}
