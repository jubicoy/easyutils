package fi.jubic.easyutils.tuple;

import java.util.Arrays;
import java.util.List;

public class Tuple2<T0, T1> extends Tuple {
    private final T0 t0;
    private final T1 t1;

    Tuple2(T0 t0, T1 t1) {
        this.t0 = t0;
        this.t1 = t1;
    }

    public T0 get0() {
        return t0;
    }

    public T1 get1() {
        return t1;
    }

    @Override
    public List<Object> toList() {
        return Arrays.asList(t0, t1);
    }
}
