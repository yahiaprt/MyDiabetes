package com.yahia.healthysiabires.partage.data.serilisations;

import androidx.annotation.Nullable;

import java.io.Serializable;

public interface Serialiser<Input, Output extends Serializable> {
    @Nullable Output serialize(Input input);
    @Nullable Input deserialize(Output output);
}
