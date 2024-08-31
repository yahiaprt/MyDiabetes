package com.yahia.healthysiabires.partage.event;

/**
 * Created by Faltenreich on 01.08.2016.
 */
public abstract class etDrivenComponent {

    public void start() {
        ets.register(this);
    }

    public void stop() {
        ets.unregister(this);
    }
}
