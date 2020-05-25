package com.atqc.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.javafaker.Faker;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private int id;
    @JsonProperty(value = "user_name")
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private UserStatus userStatus;

    public enum UserStatus {

        ACTIVE(0), INACTIVE(1);

        private final int userStatus;

        UserStatus(int userStatus) {
            this.userStatus = userStatus;
        }

        @JsonValue
        public int getUserStatus() {
            return userStatus;
        }

    }

    public static UserModel create() {
        Faker faker = new Faker();
        return  UserModel.builder()
                        .id(Integer.parseInt(faker.number().digits(5)))
                        .username(faker.name().username())
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .email(faker.internet().emailAddress())
                        .password(faker.internet().password())
                        .phone(faker.phoneNumber().phoneNumber())
                        .userStatus(UserModel.UserStatus.ACTIVE)
                        .build();
    }


}
