package com.yahia.healthysiabires.partage.data.database.entity.deprecated;

import com.yahia.healthysiabires.partage.data.database.entity.type;

/**
 * Created by Faltenreich on 05.04.2016.
 */
public enum typeDeprecated {

    BLOODSUGAR(type.BLOODSUGAR),
    BOLUS(type.INSULIN),
    MEAL(type.MEAL),
    ACTIVITY(type.ACTIVITY),
    HBA1C(type.HBA1C),
    WEIGHT(type.WEIGHT),
    PULSE(type.PULSE);

    private type category;

    typeDeprecated(type category) {
        this.category = category;
    }

    public type toUpdate() {
        return category;
    }
}
