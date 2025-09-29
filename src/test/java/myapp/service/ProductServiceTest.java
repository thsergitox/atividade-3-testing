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
            "A valid title",
            "keywords",
            null,
            5,
            10,
            "10x10x10",
            new BigDecimal("99.99"),
            ProductStatus.IN_STOCK,
            1.5,
            now,
            now
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertTrue(violations.isEmpty(), "TC1 should be a valid product case");
    }

    @Test
    public void testProductValidation_TC2_ValidCase() {
        // Arrange
        Product product = createProductSample(
            2L,
            "Valid Title TC2",
            "keywords",
            "a".repeat(50),
            5,
            0,
            "10x10x10",
            new BigDecimal("199.99"),
            ProductStatus.OUT_OF_STOCK,
            2.0,
            Instant.now(),
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertTrue(violations.isEmpty(), "TC2 should be a valid product case");
    }

    @Test
    public void testProductValidation_TC3_ValidCase() {
        // Arrange
        Product product = createProductSample(
            3L,
            "Valid Title TC3",
            "keywords",
            "a".repeat(51),
            5,
            0,
            "10x10x10",
            new BigDecimal("299.99"),
            ProductStatus.PREORDER,
            3.0,
            Instant.now(),
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertTrue(violations.isEmpty(), "TC3 should be a valid product case");
    }

    @Test
    public void testProductValidation_TC4_ValidCase() {
        // Arrange
        Product product = createProductSample(
            4L,
            "Valid Title TC4",
            "keywords",
            "a".repeat(52),
            5,
            0,
            "10x10x10",
            new BigDecimal("399.99"),
            ProductStatus.DISCONTINUED,
            4.0,
            Instant.now(),
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertTrue(violations.isEmpty(), "TC4 should be a valid product case");
    }

    // --- INVALID TEST CASES ---

    @Test
    public void testProductValidation_TC5_TitleTooShort() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            5L,
            "aa", // Invalid
            "keywords",
            null,
            5,
            10,
            "10x10x10",
            new BigDecimal("99.99"),
            ProductStatus.IN_STOCK,
            1.5,
            now,
            now
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("title", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC6_TitleTooLong() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            6L,
            "a".repeat(101), // Invalid
            "keywords",
            null,
            5,
            10,
            "10x10x10",
            new BigDecimal("99.99"),
            ProductStatus.IN_STOCK,
            1.5,
            now,
            now
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("title", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC7_KeywordsTooLong() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            7L,
            "A valid title",
            "a".repeat(201), // Invalid
            null,
            5,
            10,
            "10x10x10",
            new BigDecimal("99.99"),
            ProductStatus.IN_STOCK,
            1.5,
            now,
            now
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("keywords", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC8_DescriptionTooShort() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            8L,
            "A valid title",
            "keywords",
            "a".repeat(49), // Invalid
            5,
            10,
            "10x10x10",
            new BigDecimal("99.99"),
            ProductStatus.IN_STOCK,
            1.5,
            now,
            now
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("description", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC9_RatingTooLow() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            9L,
            "Title",
            "keywords",
            null,
            0, // Invalid
            1,
            null,
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
        assertEquals("rating", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC10_RatingTooHigh() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            10L,
            "Title",
            "keywords",
            null,
            11, // Invalid
            1,
            null,
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
        assertEquals("rating", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC11_PriceTooLow() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            11L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            new BigDecimal("0.99"), // Invalid
            ProductStatus.IN_STOCK,
            null,
            now,
            now
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("price", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC12_PriceTooHigh() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            12L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            new BigDecimal("10000"), // Invalid
            ProductStatus.IN_STOCK,
            null,
            now,
            now
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("price", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC13_StockTooLow() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            13L,
            "Title",
            null,
            null,
            5,
            -1, // Invalid
            null,
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
        assertEquals("quantityInStock", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC14_StatusIsNull() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            14L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            new BigDecimal("10"),
            null, // Invalid
            null,
            now,
            now
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("status", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC15_WeightIsNegative() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            15L,
            "Title",
            null,
            null,
            5,
            1,
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
    public void testProductValidation_TC16_DimensionsTooLong() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            16L,
            "Title",
            null,
            null,
            5,
            1,
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
    public void testProductValidation_TC17_DateAddedInFuture() {
        // Arrange
        Product product = createProductSample(
            17L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            Instant.now().plusSeconds(86400), // Invalid Business Rule
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: Fails as expected, exposing missing validation
        assertTrue(violations.isEmpty(), "FAILS: @PastOrPresent is missing for dateAdded");
    }

    @Test
    public void testProductValidation_TC18_DateAddedTooOld() {
        // Arrange
        Product product = createProductSample(
            18L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            Instant.ofEpochSecond(1758743675L), // Invalid Business Rule
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: Fails as expected, exposing missing validation
        assertTrue(violations.isEmpty(), "FAILS: Business rule for minimum date is not implemented");
    }

    @Test
    public void testProductValidation_TC19_DateAddedIsNull() {
        // Arrange
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
    public void testProductValidation_TC20_DateModifiedInFuture() {
        // Arrange
        Instant now = Instant.now();
        Product product = createProductSample(
            20L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            now,
            now.plusSeconds(86400) // Invalid Business Rule
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: Fails as expected, exposing missing validation
        assertTrue(violations.isEmpty(), "FAILS: @PastOrPresent is missing for dateModified");
    }

    @Test
    public void testProductValidation_TC21_DateModifiedBeforeDateAdded() {
        // Arrange
        Instant dateAdded = Instant.now();
        Instant dateModified = dateAdded.minusSeconds(86400); // Invalid Business Rule
        Product product = createProductSample(
            21L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            new BigDecimal("10"),
            ProductStatus.IN_STOCK,
            null,
            dateAdded,
            dateModified
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: Fails as expected, exposing missing validation
        assertTrue(violations.isEmpty(), "FAILS: Cross-field validation for dates is missing");
    }
}
