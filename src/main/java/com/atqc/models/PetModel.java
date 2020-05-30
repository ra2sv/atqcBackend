package com.atqc.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.javafaker.Faker;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetModel {

    private int id;
    private PetCategoryModel category;
    private String name;
    private List<String> photoUrls;
    private List<PetTagModel> tags;
    private String status;


    public enum PetStatus {

        AVAILABLE,
        PENDING,
        SOLD;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PetTagModel {

        private int id;
        private String name;

        public enum TagName {
            SMALL(0),
            MIDDLE(1),
            BIG(2);

            private final int value;

            TagName(int tagName) {
                this.value = tagName;
            }

            @JsonValue
            public int getTagId() {
                return value;
            }

        }

        public static PetTagModel selectTag(TagName tagName) {
            return PetTagModel.builder()
                    .id(tagName.getTagId())
                    .name(tagName.toString().toLowerCase())
                    .build();
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PetCategoryModel {

        private int id;
        private String name;

        public enum CategoryName {
            SMALL(0),
            MIDDLE(1),
            BIG(2);

            private final int value;

            CategoryName(int categoryName) {
                this.value = categoryName;
            }

            @JsonValue
            public int getCategoryId() {
                return value;
            }

        }

        public static PetCategoryModel selectCategory(CategoryName categoryName) {
            return PetCategoryModel.builder()
                    .id(categoryName.getCategoryId())
                    .name(categoryName.toString().toLowerCase())
                    .build();
        }

    }


    public static PetModel random() {
        Faker faker = new Faker();

        return  PetModel.builder()
                .id(Integer.parseInt(faker.number().digits(5)))
                .category(PetCategoryModel.selectCategory(PetCategoryModel.CategoryName.BIG))
                .name(faker.name().name())
                .photoUrls(Collections.singletonList("http://google.com"))
                .tags(Arrays.asList(PetTagModel.selectTag(PetTagModel.TagName.SMALL)))
                .status(PetStatus.AVAILABLE.toString().toLowerCase())
                .build();
    }

}
