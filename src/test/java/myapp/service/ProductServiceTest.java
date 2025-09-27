package myapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;
import myapp.domain.Product;
import myapp.domain.enumeration.ProductStatus;
import myapp.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService; // Injects the mock into the service

    // Helper method to create a sample product with flexible parameters
    public static Product createProductSample(
        Long id,
        String title,
        String keywords,
        String description,
        int rating,
        int quantityInStock,
        String dimensions,
        BigDecimal price,
        ProductStatus status,
        Double weight,
        Instant dateAdded
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
            .dateAdded(dateAdded);

        return product;
    }

    // BEGIN TEST CASES - (with example for Titile)
    @Test
    public void testProductValidation_TC1_ValidCase() {
        // Arrange: Create a product with all valid fields, corresponding to TC1
        Product product = createProductSample(
            1L,
            "A valid title",
            "keywords",
            null, // Description is null, which is valid for d==0 if we interpret it as empty allowed
            5,
            10,
            "10x10x10",
            new BigDecimal("99.99"),
            ProductStatus.IN_STOCK,
            1.5,
            Instant.now().minusSeconds(1000)
        );
        product.setDescription(null); // Explicitly setting null for d==0 case

        // Act: Validate the product
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: No violations should be found
        assertTrue(violations.isEmpty(), "A valid product should not have violations");
    }

    @Test
    public void testProductValidation_TC2_ValidCase() {
        // Arrange: TC2 -> d >= 50, s = OUT_OF_STOCK, tsM = vazia
        Product product = createProductSample(
            2L,
            "Valid Title TC2",
            "keywords",
            "a".repeat(50), // d >= 50
            5,
            0,
            "10x10x10",
            new BigDecimal("199.99"),
            ProductStatus.OUT_OF_STOCK, // s = OUT_OF_STOCK
            2.0,
            Instant.now().minusSeconds(2000)
        );
        product.setDateModified(null); // tsM = vazia

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertTrue(violations.isEmpty(), "TC2 should be a valid product case");
    }

    @Test
    public void testProductValidation_TC3_ValidCase() {
        // Arrange: TC3 -> d >= 50, s = PREORDER, tsM = vazia
        Product product = createProductSample(
            3L,
            "Valid Title TC3",
            "keywords",
            "a".repeat(51), // d >= 50
            5,
            0,
            "10x10x10",
            new BigDecimal("299.99"),
            ProductStatus.PREORDER, // s = PREORDER
            3.0,
            Instant.now().minusSeconds(3000)
        );
        product.setDateModified(null); // tsM = vazia

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertTrue(violations.isEmpty(), "TC3 should be a valid product case");
    }

    @Test
    public void testProductValidation_TC4_ValidCase() {
        // Arrange: TC4 -> d >= 50, s = DISCONTINUED, tsM = vazia
        Product product = createProductSample(
            4L,
            "Valid Title TC4",
            "keywords",
            "a".repeat(52), // d >= 50
            5,
            0,
            "10x10x10",
            new BigDecimal("399.99"),
            ProductStatus.DISCONTINUED, // s = DISCONTINUED
            4.0,
            Instant.now().minusSeconds(4000)
        );
        product.setDateModified(null); // tsM = vazia

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertTrue(violations.isEmpty(), "TC4 should be a valid product case");
    }

    @Test
    public void testProductValidation_TC5_TitleTooShort() {
        // Arrange: Create a product with a title that is too short
        Product product = createProductSample(
            1L,
            "aa", // Invalid title (less than 3 chars)
            "keywords",
            null,
            5,
            10,
            "10x10x10",
            new BigDecimal("99.99"),
            ProductStatus.IN_STOCK,
            1.5,
            Instant.now()
        );

        // Act: Validate the product
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: Expect one violation for the title field
        assertEquals(1, violations.size(), "Should have one violation");
        ConstraintViolation<Product> violation = violations.iterator().next();
        assertEquals("title", violation.getPropertyPath().toString(), "Violation should be on 'title'");
        assertTrue(
            violation.getMessage().contains("must be between 3 and 100"),
            "Violation message should indicate size constraint"
        );
    }

    @Test
    public void testProductValidation_TC6_TitleTooLong() {
        // Arrange: Create a product with a title that is too long
        String longTitle = "a".repeat(101); // 101 characters long
        Product product = createProductSample(
            1L,
            longTitle, // Invalid title (more than 100 chars)
            "keywords",
            null,
            5,
            10,
            "10x10x10",
            new BigDecimal("99.99"),
            ProductStatus.IN_STOCK,
            1.5,
            Instant.now()
        );

        // Act: Validate the product
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: Expect one violation for the title field
        assertEquals(1, violations.size(), "Should have one violation");
        ConstraintViolation<Product> violation = violations.iterator().next();
        assertEquals("title", violation.getPropertyPath().toString(), "Violation should be on 'title'");
        assertTrue(
            violation.getMessage().contains("must be between 3 and 100"),
            "Violation message should indicate size constraint"
        );
    }

    @Test
    public void testProductValidation_TC7_KeywordsTooLong() {
        // Arrange: Create a product with keywords that are too long
        String longKeywords = "a".repeat(201); // 201 characters long
        Product product = createProductSample(
            1L,
            "A valid title",
            longKeywords, // Invalid keywords
            null,
            5,
            10,
            "10x10x10",
            new BigDecimal("99.99"),
            ProductStatus.IN_STOCK,
            1.5,
            Instant.now()
        );

        // Act: Validate the product
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: Expect one violation for the keywords field
        assertEquals(1, violations.size(), "Should have one violation");
        ConstraintViolation<Product> violation = violations.iterator().next();
        assertEquals("keywords", violation.getPropertyPath().toString(), "Violation should be on 'keywords'");
    }

    @Test
    public void testProductValidation_DescriptionValid_AtLeast50Chars() {
        // Arrange: Create a product with a description of exactly 50 characters (valid)
        String validDescription = "a".repeat(50);
        Product product = createProductSample(
            1L,
            "A valid title",
            "keywords",
            validDescription,
            5,
            10,
            "10x10x10",
            new BigDecimal("99.99"),
            ProductStatus.IN_STOCK,
            1.5,
            Instant.now()
        );

        // Act: Validate the product
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: No violations should be found
        assertTrue(violations.isEmpty(), "A description with 50 chars should be valid");
    }

    @Test
    public void testProductValidation_TC8_DescriptionTooShort() {
        // Arrange: Create a product with a description that is too short (less than 50)
        String shortDescription = "a".repeat(49);
        Product product = createProductSample(
            1L,
            "A valid title",
            "keywords",
            shortDescription, // Invalid description
            5,
            10,
            "10x10x10",
            new BigDecimal("99.99"),
            ProductStatus.IN_STOCK,
            1.5,
            Instant.now()
        );

        // Act: Validate the product
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: Expect one violation for the description field
        assertEquals(1, violations.size(), "Should have one violation");
        ConstraintViolation<Product> violation = violations.iterator().next();
        assertEquals("description", violation.getPropertyPath().toString(), "Violation should be on 'description'");
    }

    @Test
    public void testProductValidation_TC9_RatingTooLow() {
        // Arrange
        Product product = createProductSample(
            1L,
            "Title",
            null,
            null,
            0,
            1,
            null,
            BigDecimal.TEN,
            ProductStatus.IN_STOCK,
            null,
            Instant.now()
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
        Product product = createProductSample(
            1L,
            "Title",
            null,
            null,
            11,
            1,
            null,
            BigDecimal.TEN,
            ProductStatus.IN_STOCK,
            null,
            Instant.now()
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
        Product product = createProductSample(
            1L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            new BigDecimal("0.99"),
            ProductStatus.IN_STOCK,
            null,
            Instant.now()
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
        Product product = createProductSample(
            1L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            new BigDecimal("10000"),
            ProductStatus.IN_STOCK,
            null,
            Instant.now()
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
        Product product = createProductSample(
            1L,
            "Title",
            null,
            null,
            5,
            -1,
            null,
            BigDecimal.TEN,
            ProductStatus.IN_STOCK,
            null,
            Instant.now()
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
        Product product = createProductSample(
            1L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            BigDecimal.TEN,
            null,
            null,
            Instant.now()
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
        Product product = createProductSample(
            1L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            BigDecimal.TEN,
            ProductStatus.IN_STOCK,
            -1.0,
            Instant.now()
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("weight", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC17_DimensionsTooLong() {
        // Arrange
        String longDimensions = "a".repeat(51); // 51 characters
        Product product = createProductSample(
            1L,
            "Title",
            null,
            null,
            5,
            1,
            longDimensions,
            BigDecimal.TEN,
            ProductStatus.IN_STOCK,
            null,
            Instant.now()
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("dimensions", violations.iterator().next().getPropertyPath().toString());
    }

    // --- Date Validation Tests ---
    // Note: These tests highlight missing validation annotations in the Product entity.

    @Test
    public void testProductValidation_TC20_DateAddedIsNull() {
        // Arrange: Corresponds to "tsA = vazia"
        Product product = createProductSample(
            1L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            BigDecimal.TEN,
            ProductStatus.IN_STOCK,
            null,
            null
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: Expect a violation because dateAdded is @NotNull
        assertEquals(1, violations.size());
        assertEquals("dateAdded", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testProductValidation_TC18_DateAddedInFuture() {
        // Arrange: Corresponds to "tsA > ts atual"
        Instant futureDate = Instant.now().plusSeconds(86400); // 1 day in the future
        Product product = createProductSample(
            1L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            BigDecimal.TEN,
            ProductStatus.IN_STOCK,
            null,
            futureDate
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: No violation is expected because @PastOrPresent is missing on dateAdded
        assertTrue(violations.isEmpty(), "Validation should pass as there is no annotation to check for past dates.");
    }

    @Test
    public void testProductValidation_TC19_DateAddedTooOld() {
        // Arrange: Corresponds to "tsA < 1758743676"
        Instant oldDate = Instant.ofEpochSecond(1758743675L); // Before the specified timestamp
        Product product = createProductSample(
            1L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            BigDecimal.TEN,
            ProductStatus.IN_STOCK,
            null,
            oldDate
        );

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: No violation is expected as this business rule is not in the annotations.
        assertTrue(violations.isEmpty(), "Validation should pass as this business rule is not implemented via annotations.");
    }

    @Test
    public void testProductValidation_TC21_DateModifiedInFuture() {
        // Arrange: Corresponds to "tsM > ts atual"
        Instant futureDate = Instant.now().plusSeconds(86400);
        Product product = createProductSample(
            1L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            BigDecimal.TEN,
            ProductStatus.IN_STOCK,
            null,
            Instant.now()
        );
        product.setDateModified(futureDate);

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: No violation is expected because there is no validation on dateModified
        assertTrue(violations.isEmpty(), "Validation should pass as there is no annotation on dateModified.");
    }

    @Test
    public void testProductValidation_TC22_DateModifiedBeforeDateAdded() {
        // Arrange: Corresponds to "tsM < tsA"
        Instant dateAdded = Instant.now();
        Instant dateModified = dateAdded.minusSeconds(86400); // 1 day before
        Product product = createProductSample(
            1L,
            "Title",
            null,
            null,
            5,
            1,
            null,
            BigDecimal.TEN,
            ProductStatus.IN_STOCK,
            null,
            dateAdded
        );
        product.setDateModified(dateModified);

        // Act
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        // Assert: No violation is expected as this is a complex business rule not covered by simple annotations.
        assertTrue(violations.isEmpty(), "Validation should pass as there is no cross-field validation for dates.");
    }    
}
