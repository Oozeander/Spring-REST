package com.example.springweb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonPropertyOrder({"id", "name", "description", "created_at", "price"})
public class Product {

    @JsonProperty
    UUID id;
    @JsonProperty
    @NotBlank(message = "{product.name.not-empty}")
    String name;
    @JsonProperty
    @Size(min = 12, max = 255, message = "{product.description.size}")
    String description;
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.SSS")
    @Past(message = "{product.created-at.past}")
    LocalDateTime createdAt;
    @JsonProperty
    @Min(value = 1, message = "{product.price.min}")
    double price;
}
