package com.atqc.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.*;

import static com.atqc.models.PetCategoryModel.*;
import static com.atqc.models.PetTagModel.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetModel {

    private int id;
    @JsonProperty(value = "category")
    private PetCategoryModel categoryObject;
    private String name;
    private ArrayList<String> photoUrls;
    private ArrayList < PetTagModel > tags = new ArrayList < PetTagModel > ();
    private PetStatus status;


    public enum PetStatus {

        AVAILABLE("available"),
        PENDING("pending"),
        SOLD("sold");

        private final String value;

        PetStatus(String petStatus) {
            this.value = petStatus;
        }
        @JsonValue

        public String getPetStatus() {
            return value;
        }

    }

    public static PetModel createDog() {
        Faker faker = new Faker();

        return  PetModel.builder()
                .id(Integer.parseInt(faker.number().digits(5)))
                .categoryObject(selectDogs())
                .name(faker.name().name())
                .photoUrls(new ArrayList<String>(Collections.singleton("http://google.com")))
                .tags(new ArrayList<PetTagModel>(Collections.singleton(selectBig())))
                .status(PetStatus.AVAILABLE)
                .build();
    }

    public static PetModel withoutRequired() {
        Faker faker = new Faker();

        return  PetModel.builder()
                .id(Integer.parseInt(faker.number().digits(5)))
                .categoryObject(selectBirds())
                .photoUrls(new ArrayList<String>(Collections.singleton("http://google.com")))
                .tags(new ArrayList<PetTagModel>(Collections.singleton(selectMiddle())))
                .status(PetStatus.AVAILABLE)
                .build();
    }

}
