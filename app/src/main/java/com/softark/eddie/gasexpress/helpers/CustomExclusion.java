package com.softark.eddie.gasexpress.helpers;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by Eddie on 5/10/2017.
 */

public class CustomExclusion implements ExclusionStrategy {

    public final Class<?> excludedClass;

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
