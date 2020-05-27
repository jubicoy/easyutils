package fi.jubic.easyutils.tuple;

import java.util.Arrays;
import java.util.List;

public class Tuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> extends Tuple {
    private final T0 t0;
    private final T1 t1;
    private final T2 t2;
    private final T3 t3;
    private final T4 t4;
    private final T5 t5;
    private final T6 t6;
    private final T7 t7;
    private final T8 t8;

    Tuple9(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        this.t0 = t0;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.t5 = t5;
        this.t6 = t6;
        this.t7 = t7;
        this.t8 = t8;
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

    public T5 get5() {
        return t5;
    }

    public T6 get6() {
        return t6;
    }

    public T7 get7() {
        return t7;
    }

    public T8 get8() {
        return t8;
    }

    @Override
    public List<Object> toList() {
        return Arrays.asList(t0, t1, t2, t3, t4, t5, t6, t7, t8);
    }
}
