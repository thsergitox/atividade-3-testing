package myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A WishList.
 */
@Entity
@Table(name = "wish_list")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WishList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "restricted")
    private Boolean restricted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "wishList")
    @JsonIgnoreProperties(value = { "wishList", "order", "categories" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "wishLists", "addresses", "orders" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WishList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public WishList title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getRestricted() {
        return this.restricted;
    }

    public WishList restricted(Boolean restricted) {
        this.setRestricted(restricted);
        return this;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setWishList(null));
        }
        if (products != null) {
            products.forEach(i -> i.setWishList(this));
        }
        this.products = products;
    }

    public WishList products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public WishList addProduct(Product product) {
        this.products.add(product);
        product.setWishList(this);
        return this;
    }

    public WishList removeProduct(Product product) {
        this.products.remove(product);
        product.setWishList(null);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public WishList customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WishList)) {
            return false;
        }
        return getId() != null && getId().equals(((WishList) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WishList{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", restricted='" + getRestricted() + "'" +
            "}";
    }
}
