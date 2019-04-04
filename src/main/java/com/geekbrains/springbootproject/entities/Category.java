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

    @ManyToOne
    @JoinColumn(name = "parent")
    private Category parent;

    public List<Category> getHierarchy(List<Category> hrList){
        if(parent != null){
            hrList = parent.getHierarchy(hrList);
        }
        hrList.add(this);
        return hrList;
    }
}
