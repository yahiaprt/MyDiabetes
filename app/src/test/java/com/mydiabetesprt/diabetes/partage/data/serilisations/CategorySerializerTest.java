package com.mydiabetesprt.diabetes.partage.data.serilisations;

import com.mydiabetesprt.diabetes.partage.data.database.entity.type;

import org.junit.Assert;
import org.junit.Test;

public class CategorySerializerTest {

    private type[] expectedInput = new type[] {
        type.BLOODSUGAR,
        type.INSULIN,
        type.MEAL,
        type.ACTIVITY,
        type.HBA1C,
        type.WEIGHT,
        type.PULSE,
        type.PRESSURE,
        type.OXYGEN_SATURATION
    };
    private String expectedOutput = "1;2;3;4;5;6;7;8;9";

    @Test
    public void serializes() {
        String output = new typeSerialiser().serialize(expectedInput);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void deserializes() {
        type[] input = new typeSerialiser().deserialize(expectedOutput);
        Assert.assertArrayEquals(expectedInput, input);
    }
}