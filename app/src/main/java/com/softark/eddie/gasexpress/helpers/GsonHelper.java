package com.softark.eddie.gasexpress.helpers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import io.realm.RealmObject;

class GsonHelper {

    public static final class HELPER {
        private static final GsonBuilder GSON = new GsonBuilder();

        public static GsonBuilder getBuilder() {
            GSON.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
            GSON.excludeFieldsWithoutExposeAnnotation();
            GSON.serializeNulls();
            GSON.setExclusionStrategies(new CustomExclusion(RealmObject.class));
            return GSON;
        }
    }

    public static GsonBuilder getBuilder() {
        return HELPER.getBuilder();
    }

}
