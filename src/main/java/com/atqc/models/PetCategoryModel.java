package com.atqc.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetCategoryModel {

    private CategoryId id;
    private CategoryName name;

    public enum CategoryId {
        DOGS(1),
        CATS(2),
        BIRDS(3);

        private int value;

        CategoryId(int categoryId) {
            this.value = categoryId;
        }

        @JsonValue
        public int getCategoryId() {
            return value;
        }

    }

    public enum CategoryName {
        DOGS("Dogs"),
        CATS("Cats"),
        BIRDS("Birds");

        private final String value;

        CategoryName(String categoryName) {
            this.value = categoryName;
        }

        @JsonValue
        public String getCategoryName() {
            return value;
        }
    }

    public static PetCategoryModel selectDogs() {
        return PetCategoryModel.builder()
                .id(CategoryId.DOGS)
                .name(CategoryName.DOGS)
                .build();
    }

    public static PetCategoryModel selectCats() {
        return PetCategoryModel.builder()
                .id(PetCategoryModel.CategoryId.CATS)
                .name(PetCategoryModel.CategoryName.CATS)
                .build();
    }

    public static PetCategoryModel selectBirds() {
        return PetCategoryModel.builder()
                .id(CategoryId.BIRDS)
                .name(CategoryName.BIRDS)
                .build();
    }

}
