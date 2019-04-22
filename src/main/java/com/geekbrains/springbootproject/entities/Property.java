package com.geekbrains.springbootproject.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "properties")
@Data
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    transient private String valueFirst;
    transient private String valueSecond;

    @Column(name = "title")
    private String title;

    @OneToOne
    @JoinColumn(name = "elem_id")
    private Element element;

}
