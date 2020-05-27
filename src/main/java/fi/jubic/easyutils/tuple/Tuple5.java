package fi.jubic.easyutils.tuple;

import java.util.Arrays;
import java.util.List;

public class Tuple5<T0, T1, T2, T3, T4> extends Tuple {
    private final T0 t0;
    private final T1 t1;
    private final T2 t2;
    private final T3 t3;
    private final T4 t4;

    Tuple5(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4) {
        this.t0 = t0;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
    }

    public T0 get0() {
        return t0;
    }

    public T1 get1() {
        return t1;
    }

    public T2 get2() {
        return t2;
    }

    public T3 get3() {
        return t3;
    }

    public T4 get4() {
        return t4;
    }

    @Override
    public List<Object> toList() {
        return Arrays.asList(t0, t1, t2, t3, t4);
    }
}
