package com.example.springweb.endpoint;

import com.example.springweb.model.Product;
import com.example.springweb.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*@CrossOrigin(origins = "http://www.example.com", maxAge = 36000,
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE})*/
@Tag(name = "Product Endpoint",
        description = "Endpoint for accessing all kinds of query or change state of products currently available")
@Validated
@RestController
@RequestMapping(value = "products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products", method = "GET",
        security = {@SecurityRequirement(name = "none")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Product.class)))}),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(value = "maximalPrice") Optional<Double> maximalPrice,
            @RequestParam(value = "minimalPrice") Optional<Double> minimalPrice
    ) {
        return ResponseEntity.ok(productService.getProducts(maximalPrice, minimalPrice));
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Product> getProductById(
            @PathVariable("id") UUID id
    ) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> postProduct(
            @RequestBody @Valid Product product
    ) {
        var id = productService.createProduct(product);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(id)
                        .toUri()
        ).build();
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Product> putProduct(
            @PathVariable("id") UUID id,
            @RequestBody @Valid Product product
    ) {
        return new ResponseEntity<>(productService.updateProduct(id, product), getLocationHeader(product.id()), HttpStatus.OK);
    }

    @PatchMapping(value = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Product> patchProductByPrice(
            @PathVariable("id") UUID id,
            @RequestParam("price") @Min(1) double price
    ) {
        return new ResponseEntity<>(productService.updateProductPrice(id, price), getLocationHeader(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("id") UUID id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    private HttpHeaders getLocationHeader(UUID id) {
        var headers = new HttpHeaders();
        headers.add("Location", ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toString());
        return headers;
    }
}
