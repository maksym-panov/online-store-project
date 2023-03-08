package com.panov.store.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "ProductType")
@Table(name = "ProductType")
@JsonIgnoreProperties({ "products" })
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productTypeId;
    @NaturalId
    private String name;

    @ManyToMany
    private Set<Product> products = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof ProductType other)) return false;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return String.format("ProductType[id = %d, name = %s]",
                productTypeId,
                name
        );
    }
}
