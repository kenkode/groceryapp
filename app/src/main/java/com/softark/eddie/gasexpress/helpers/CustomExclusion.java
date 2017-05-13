package com.softark.eddie.gasexpress.helpers;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

class CustomExclusion implements ExclusionStrategy {

    private final Class<?> excludedClass;

    public CustomExclusion(Class<?> excludedClass) {
        this.excludedClass = excludedClass;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return excludedClass.equals(f.getDeclaredClass());
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return excludedClass.equals(clazz);
    }
}
