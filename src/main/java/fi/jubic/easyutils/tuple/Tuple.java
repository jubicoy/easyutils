package fi.jubic.easyutils.tuple;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A typed tuple implementation. This class is intended to be used mostly in lambdas passed to
 * methods of other classes in this library.
 */
public abstract class Tuple {
    /**
     * Return elements of this {@code Tuple} wrapped in a simple {@link List}.
     *
     * @return the list of values
     */
    public abstract List<Object> toList();

    @Override
    public String toString() {
        return String.format(
                "[%s]",
                toList()
                        .stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "))
        );
    }

    /**
     * Create a {@code Tuple} of 2 elements.
     *
     * @param t0 the the first value
     * @param t1 the the second value
     * @param <T0> the the type of the first value
     * @param <T1> the the type of the second value
     * @return the created tuple
     */
    public static <T0, T1> Tuple2<T0, T1> of(T0 t0, T1 t1) {
        return new Tuple2<>(t0, t1);
    }

    /**
     * Create a {@code Tuple} of 3 elements.
     *
     * @param t0 the the first value
     * @param t1 the the second value
     * @param t2 the the third value
     * @param <T0> the the type of the first value
     * @param <T1> the the type of the second value
     * @param <T2> the the type of the third value
     * @return the created tuple
     */
    public static <T0, T1, T2> Tuple3<T0, T1, T2> of(T0 t0, T1 t1, T2 t2) {
        return new Tuple3<>(t0, t1, t2);
    }

    /**
     * Create a {@code Tuple} of 4 elements.
     *
     * @param t0 the first value
     * @param t1 the second value
     * @param t2 the third value
     * @param t3 the fourth value
     * @param <T0> the type of the first value
     * @param <T1> the type of the second value
     * @param <T2> the type of the third value
     * @param <T3> the type of the fourth value
     * @return the created tuple
     */
    public static <T0, T1, T2, T3> Tuple4<T0, T1, T2, T3> of(T0 t0, T1 t1, T2 t2, T3 t3) {
        return new Tuple4<>(t0, t1, t2, t3);
    }

    /**
     * Create a {@code Tuple} of 5 elements.
     *
     * @param t0 the first value
     * @param t1 the second value
     * @param t2 the third value
     * @param t3 the fourth value
     * @param t4 the 5th value
     * @param <T0> the type of the first value
     * @param <T1> the type of the second value
     * @param <T2> the type of the third value
     * @param <T3> the type of the fourth value
     * @param <T4> the type of the 5th value
     * @return the created tuple
     */
    public static <T0, T1, T2, T3, T4> Tuple5<T0, T1, T2, T3, T4> of(
            T0 t0,
            T1 t1,
            T2 t2,
            T3 t3,
            T4 t4
    ) {
        return new Tuple5<>(t0, t1, t2, t3, t4);
    }

    /**
     * Create a {@code Tuple} of 6 elements.
     *
     * @param t0 the first value
     * @param t1 the second value
     * @param t2 the third value
     * @param t3 the fourth value
     * @param t4 the 5th value
     * @param t5 the 6th value
     * @param <T0> the type of the first value
     * @param <T1> the type of the second value
     * @param <T2> the type of the third value
     * @param <T3> the type of the fourth value
     * @param <T4> the type of the 5th value
     * @param <T5> the type of the 6th value
     * @return the created tuple
     */
    public static <T0, T1, T2, T3, T4, T5> Tuple6<T0, T1, T2, T3, T4, T5> of(
            T0 t0,
            T1 t1,
            T2 t2,
            T3 t3,
            T4 t4,
            T5 t5
    ) {
        return new Tuple6<>(t0, t1, t2, t3, t4, t5);
    }

    /**
     * Create a {@code Tuple} of 7 elements.
     * 
     * @param t0 the first value
     * @param t1 the second value
     * @param t2 the third value
     * @param t3 the fourth value
     * @param t4 the 5th value
     * @param t5 the 6th value
     * @param t6 the 7th value
     * @param <T0> the type of the first value
     * @param <T1> the type of the second value
     * @param <T2> the type of the third value
     * @param <T3> the type of the fourth value
     * @param <T4> the type of the 5th value
     * @param <T5> the type of the 6th value
     * @param <T6> the type of the 7th value
     * @return the created tuple
     */
    public static <T0, T1, T2, T3, T4, T5, T6> Tuple7<T0, T1, T2, T3, T4, T5, T6> of(
            T0 t0,
            T1 t1,
            T2 t2,
            T3 t3,
            T4 t4,
            T5 t5,
            T6 t6
    ) {
        return new Tuple7<>(t0, t1, t2, t3, t4, t5, t6);
    }

    /**
     * Create a {@code Tuple} of 8 elements.
     * @param t0 the first value
     * @param t1 the second value
     * @param t2 the third value
     * @param t3 the fourth value
     * @param t4 the 5th value
     * @param t5 the 6th value
     * @param t6 the 7th value
     * @param t7 the 8th value
     * @param <T0> the type of the first value
     * @param <T1> the type of the second value
     * @param <T2> the type of the third value
     * @param <T3> the type of the fourth value
     * @param <T4> the type of the 5th value
     * @param <T5> the type of the 6th value
     * @param <T6> the type of the 7th value
     * @param <T7> the type of the 8th value
     * @return the created tuple
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7> Tuple8<T0, T1, T2, T3, T4, T5, T6, T7> of(
            T0 t0,
            T1 t1,
            T2 t2,
            T3 t3,
            T4 t4,
            T5 t5,
            T6 t6,
            T7 t7
    ) {
        return new Tuple8<>(t0, t1, t2, t3, t4, t5, t6, t7);
    }

    /**
     * Create a {@code Tuple} of 9 elements.
     * @param t0 the first value
     * @param t1 the second value
     * @param t2 the third value
     * @param t3 the fourth value
     * @param t4 the 5th value
     * @param t5 the 6th value
     * @param t6 the 7th value
     * @param t7 the 8th value
     * @param t8 the 9th value
     * @param <T0> the type of the first value
     * @param <T1> the type of the second value
     * @param <T2> the type of the third value
     * @param <T3> the type of the fourth value
     * @param <T4> the type of the 5th value
     * @param <T5> the type of the 6th value
     * @param <T6> the type of the 7th value
     * @param <T7> the type of the 8th value
     * @param <T8> the type of the 9th value
     * @return the created tuple
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8>
                    Tuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> of(
            T0 t0,
            T1 t1,
            T2 t2,
            T3 t3,
            T4 t4,
            T5 t5,
            T6 t6,
            T7 t7,
            T8 t8
    ) {
        return new Tuple9<>(t0, t1, t2, t3, t4, t5, t6, t7, t8);
    }

    /**
     * Create a {@code Tuple} of 10 elements.
     *
     * @param t0 the first value
     * @param t1 the second value
     * @param t2 the third value
     * @param t3 the fourth value
     * @param t4 the 5th value
     * @param t5 the 6th value
     * @param t6 the 7th value
     * @param t7 the 8th value
     * @param t8 the 9th value
     * @param t9 the 10th value
     * @param <T0> the type of the first value
     * @param <T1> the type of the second value
     * @param <T2> the type of the third value
     * @param <T3> the type of the fourth value
     * @param <T4> the type of the 5th value
     * @param <T5> the type of the 6th value
     * @param <T6> the type of the 7th value
     * @param <T7> the type of the 8th value
     * @param <T8> the type of the 9th value
     * @param <T9> the type of the 10th value
     * @return the created tuple
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>
                    Tuple10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> of(
            T0 t0,
            T1 t1,
            T2 t2,
            T3 t3,
            T4 t4,
            T5 t5,
            T6 t6,
            T7 t7,
            T8 t8,
            T9 t9
    ) {
        return new Tuple10<>(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }
}
