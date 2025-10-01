package myapp.service;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import myapp.domain.Product;
import myapp.domain.enumeration.ProductStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProductServiceTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Helper method to create a product sample based on specific test case data
    public static Product createProductSample(
        Long id,
        String title,
        String keywords,
        String description,
        Integer rating,
        Integer quantityInStock,
        String dimensions,
        BigDecimal price,
        ProductStatus status,
        Double weight,
        Instant dateAdded,
        Instant dateModified
    ) {
        Product product = new Product()
            .id(id)
            .title(title)
            .keywords(keywords)
            .description(description)
            .rating(rating)
            .quantityInStock(quantityInStock)
            .dimensions(dimensions)
            .price(price)
            .status(status)
            .weight(weight)
            .dateAdded(dateAdded)
            .dateModified(dateModified);

        return product;
    }

    // --- VALID TEST CASES ---

    @Test
    public void testProductValidation_TC1_ValidCase() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            1L,
            "TC1",
            null,
            null,
            null,
            0,
            null,
            new BigDecimal("1"),
            ProductStatus.IN_STOCK,
            null,
            now,
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertTrue(violations.isEmpty(), "TC1 should be a valid product case");
    }

    @Test
    public void testProductValidation_TC2_ValidCase() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            2L,
            "TC_2",
            "",
            "a".repeat(50),
            1,
            1,
            "",
            new BigDecimal("2"),
            ProductStatus.OUT_OF_STOCK,
            0.0,
            now.minusSeconds(1),
            now
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertTrue(violations.isEmpty(), "TC2 should be a valid product case");
    }

    @Test
    public void testProductValidation_TC3_ValidCase() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            3L,
            "a".repeat(99),
            "a",
            "a".repeat(51),
            2,
            1,
            "a",
            new BigDecimal("9998"),
            ProductStatus.PREORDER,
            1.0,
            now,
            now.plusSeconds(1)
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertTrue(violations.isEmpty(), "TC3 should be a valid product case");
    }

    @Test
    public void testProductValidation_TC4_ValidCase() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            4L,
            "a".repeat(100),
            "a".repeat(199),
            null,
            9,
            1,
            "a".repeat(49),
            new BigDecimal("9999"),
            ProductStatus.DISCONTINUED,
            1.0,
            now,
            now.plusSeconds(2)
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertTrue(violations.isEmpty(), "TC4 should be a valid product case");
    }

    @Test
    public void testProductValidation_TC5_ValidCase() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            4L,
            "Valid",
            "a".repeat(200),
            null,
            10,
            1,
            "a".repeat(50),
            new BigDecimal("9999"),
            ProductStatus.DISCONTINUED,
            1.0,
            now,
            now
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertTrue(violations.isEmpty(), "TC5 should be a valid product case");
    }

    // --- INVALID TEST CASES ---

    @Test
    public void testProductValidation_TC6_TitleTooShort() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            5L,
            "aa", // Invalid
            null,
            null,
            null,
            0,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            now,
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("title", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC7_TitleTooLong() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            6L,
            "a".repeat(101), // Invalid
            null,
            null,
            null,
            0,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            now,
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("title", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC8_KeywordsTooLong() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            7L,
            "Title",
            "a".repeat(201), // Invalid
            null,
            null,
            0,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            now,
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("keywords", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC9_DescriptionTooShort() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            8L,
            "Title",
            null,
            "a".repeat(49), // Invalid
            null,
            0,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            now,
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("description", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC10_RatingTooLow() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            9L,
            "Title",
            null,
            null,
            0, // Invalid
            0,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            now,
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("rating", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC11_RatingTooHigh() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            10L,
            "Title",
            null,
            null,
            11, // Invalid
            0,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            now,
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("rating", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC12_PriceTooLow() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            11L,
            "Title",
            null,
            null,
            null,
            0,
            null,
            new BigDecimal("0"), // Invalid
            ProductStatus.IN_STOCK,
            null,
            now,
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("price", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC13_PriceTooHigh() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            12L,
            "Title",
            null,
            null,
            null,
            0,
            null,
            new BigDecimal("10000"), // Invalid
            ProductStatus.IN_STOCK,
            null,
            now,
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("price", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC14_StockTooLow() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            13L,
            "Title",
            null,
            null,
            null,
            -1, // Invalid
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            now,
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("quantityInStock", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC15_StatusUnexpected() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            14L,
            "Title",
            null,
            null,
            null,
            0,
            null,
            new BigDecimal("10"),
            null, // Invalid
            null,
            now,
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("status", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC16_WeightIsNegative() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            15L,
            "Title",
            null,
            null,
            null,
            0,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            -1.0, // Invalid
            now,
            now
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("weight", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC18_DimensionsTooLong() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            16L,
            "Title",
            null,
            null,
            null,
            0,
            "a".repeat(51), // Invalid
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            now,
            now
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("dimensions", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC19_DateAddedInFuture() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            17L,
            "Title",
            null,
            null,
            null,
            0,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            now.plusSeconds(1), // Invalid Business Rule
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: Fails as expected, exposing missing validation
        assertTrue(violations.isEmpty(), "FAILS: @PastOrPresent is missing for dateAdded");
    }

    @Test
    public void testProductValidation_TC20_DateAddedIsNull() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            19L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            null, // Invalid
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("dateAdded", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC21_DateModifiedIsDateAdded() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            21L,
            "Title",
            null,
            null,
            null,
            0,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            now,
            now
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: Fails as expected, exposing missing validation
        assertTrue(violations.isEmpty(), "FAILS: Cross-field validation for dates is missing");
    }

    @Test
    public void testProductValidation_TC22_DateModifiedInFuture() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            20L,
            "Title",
            null,
            null,
            null,
            0,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            now,
            now.plusSeconds(1) // Invalid Business Rule
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: Fails as expected, exposing missing validation
        assertTrue(violations.isEmpty(), "FAILS: @PastOrPresent is missing for dateModified");
    }

}
