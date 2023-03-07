package com.mydiabetesprt.diabetes.partage.data.premier;

import androidx.annotation.NonNull;

public class Victor2D {

    public int x;
    public int y;

    public Victor2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%d, %d", x, y);
    }
}
