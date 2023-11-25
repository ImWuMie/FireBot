package dev.wumie.system;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.Arrays;

public class GroupConfig {
    @SerializedName("enable")
    public boolean enable;

    public GroupConfig apply(GroupConfig other) {
        try {
            Field[] fields = other.getClass().getDeclaredFields();
            Field[] thisFs = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(SerializedName.class)) {
                    String fieldName = field.getName();
                    Field target = Arrays.stream(thisFs).filter((f) -> f.getName().equals(fieldName)).findFirst().orElse(null);
                    if (target != null && field.get(this) == null) {
                        target.set(this, field.get(other));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public static GroupConfig newConfig() {
        GroupConfig config = new GroupConfig();
        config.enable = true;
        return config;
    }
}
