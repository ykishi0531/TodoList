package com.example.enums;

import java.util.HashMap;
import java.util.Map;

public enum PriorityEnum {

    HIGHT(1), MIDDLE(2), LOW(3);

    private final Integer id;

    private PriorityEnum(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static PriorityEnum idOf(final Integer id) {
        return idToEmun.get(id);
    }

    private static final Map<Integer, PriorityEnum> idToEmun = new HashMap<Integer, PriorityEnum>() {
        {
            for (PriorityEnum enm : PriorityEnum.values()) {
                put(enm.getId(), enm);
            }
        }
    };
}
