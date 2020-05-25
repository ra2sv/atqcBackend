package com.atqc.models;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetTagModel {

    private TagId id;
    private TagName name;
    
    public enum TagId {
        SMALL(1),
        MIDDLE(2),
        BIG(3);

        private int value;

        TagId(int tagId) {
            this.value = tagId;
        }

        @JsonValue
        public int getTagId() {
            return value;
        }

    }

    public enum TagName {
        SMALL("Small"),
        MIDDLE("Middle"),
        BIG("Big");

        private final String value;

        TagName(String TagName) {
            this.value = TagName;
        }

        @JsonValue
        public String getTagName() {
            return value;
        }
    }
    
    public static PetTagModel selectSmall() {
        return PetTagModel.builder()
                .id(TagId.SMALL)
                .name(TagName.SMALL)
                .build();
    }
    
    public static PetTagModel selectMiddle() {
        return PetTagModel.builder()
                .id(TagId.MIDDLE)
                .name(TagName.MIDDLE)
                .build();
    }
    
    public static PetTagModel selectBig() {
        return PetTagModel.builder()
                .id(TagId.BIG)
                .name(TagName.BIG)
                .build();
    }

    public static ArrayList< PetTagModel > tags = new ArrayList < PetTagModel > ();
    static {
        tags.add(selectSmall());
        tags.add(selectMiddle());
        tags.add(selectBig());
    }
}
