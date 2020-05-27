package fi.jubic.easyutils.transactional;

import fi.jubic.easyutils.tuple.Tuple;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A {@code Transactional} represents the result of a possibly transactional computation.
 * Methods are provided to chain transactional procedures together using an API inspired by
 * {@link java.util.Optional} and {@link java.util.stream.Stream}.
 *
 * @param <T> the type of the result.
 * @param <C> the type of transaction context.
 */
public class Transactional<T, C> {
    final Function<C, T> procedure;
    final TransactionProvider<C> provider;

    protected Transactional(
            Function<C, T> procedure,
            TransactionProvider<C> provider
    ) {
        this.procedure = procedure;
        this.provider = provider;
    }

    /**
     * Returns the result acquired by performing the contained procedure in a transactional scope.
     * If an exception is thrown during the procedure, the transaction is rolled back and the
     * exception is rethrown. Otherwise the transaction is committed and the result is returned.
     *
     * @return the acquired result
     */
    public T get() {
        return provider.runWithTransaction(procedure);
    }

    /**
     * Returns the result acquired by performing the contained procedure without the transactional
     * scope. The {@link TransactionProvider} implementation may not necessarily support this
     * operation and can throw a {@link UnsupportedOperationException}.
     *
     * @return the acquired result.
     */
    public T bypass() {
        return provider.runWithoutTransaction(procedure);
    }

    /**
     * Returns a {@code Transactional} describing the result of applying the given mapping function
     * to the result of the contained procedure. If the mapping function throws an
     * {@code Exception}, the transaction will be rolled back as if the procedure had failed.
     *
     * @param mapper the mapping function
     * @param <U> the type of the result returned from the mapping function
     * @return the new {@code Transactional}
     */
    public <U> Transactional<U, C> map(Function<T, U> mapper) {
        Objects.requireNonNull(mapper);
        return new Transactional<>(
                mapper.compose(procedure),
                provider
        );
    }

    /**
     * Returns a {@code Transactional} describing the result acquired by chaining two transactional
     * procedures together. The resulting procedure will call the mapping function with the result
     * of the contained procedure and then it will perform the procedure returned by the mapping
     * function.
     *
     * @param mapper the mapping function
     * @param <U> the type of the result of the {@code Transactional} returned from the mapping
     *            function
     * @return the new {@code Transactional}
     */
    public <U> Transactional<U, C> flatMap(Function<T, Transactional<U, C>> mapper) {
        Objects.requireNonNull(mapper);
        return new Transactional<>(
                context -> mapper.apply(procedure.apply(context)).procedure.apply(context),
                provider
        );
    }

    /**
     * Returns a new {@code Transactional} describing the result of this transaction, additionally
     * performing the provided action on the result.
     *
     * @param action A non-interfering action to be performed on the result before yielding it
     * @return the new {@code Transactional}
     */
    public Transactional<T, C> peek(Consumer<T> action) {
        Objects.requireNonNull(action);
        return new Transactional<>(
                context -> {
                    T value = procedure.apply(context);
                    action.accept(value);
                    return value;
                },
                provider
        );
    }

    /**
     * Returns a new {@code Transactional} describing the result of this transaction, additionally
     * performing the provided procedure on the result and appending the {@code Transactional}
     * returned from the procedure to the new procedure chain.
     *
     * @param procedure the procedure to be applied to the procedure chain
     * @return the new {@code Transactional}
     */
    public Transactional<T, C> peekMap(Function<T, Transactional<Void, C>> procedure) {
        Objects.requireNonNull(procedure);
        return new Transactional<>(
                context -> {
                    T value = this.procedure.apply(context);
                    procedure.apply(value).procedure.apply(context);
                    return value;
                },
                provider
        );
    }

    /**
     * Returns a {@code Transactional} describing the result of the given procedure performed in a
     * transactional scope provided by the {@link TransactionProvider}.
     *
     * @param materialize the contained procedure
     * @param provider the provider for the procedure's transaction context.
     * @param <T> the type of the result of the contained procedure
     * @param <C> the type of the transaction context
     * @return the new {@code Transactional}
     */
    public static <T, C> Transactional<T, C> of(
            Function<C, T> materialize,
            TransactionProvider<C> provider
    ) {
        return new Transactional<>(materialize, provider);
    }

    /**
     * Returns a {@code Transactional} describing the result fo the given procedure performed in a
     * transactional scope of the thread calling {@link Transactional#get()}.
     *
     * <p>
     *     This is a helper creator intended to be used with thread context
     *     {@link TransactionProvider}s.
     * </p>
     *
     * @param materialize the contained procedure
     * @param provider the thread context transaction context provider
     * @param <T> the type of the result of the contained procedure
     * @return the new {@code Transactional}
     */
    public static <T> Transactional<T, Void> of(
            Supplier<T> materialize,
            TransactionProvider<Void> provider
    ) {
        return new Transactional<>(
                context -> materialize.get(),
                provider
        );
    }

    /**
     * A utility method for combining multiple {@code Transactional} results. The procedures are
     * executed sequentially. If any of the procedures throws, the transaction will be rolled
     * back.
     */
    public static <T1, T2, C> Transactional2<T1, T2, C> all(
            Transactional<T1, C> t1,
            Transactional<T2, C> t2
    ) {
        validateProviders(t1.provider, t2.provider);
        return new Transactional2<>(
                context -> Tuple.of(
                        t1.procedure.apply(context),
                        t2.procedure.apply(context)
                ),
                t1.provider
        );
    }

    /**
     * A utility method for combining multiple {@code Transactional} results. The procedures are
     * executed sequentially. If any of the procedures throws, the transaction will be rolled
     * back.
     */
    public static <T1, T2, T3, C> Transactional3<T1, T2, T3, C> all(
            Transactional<T1, C> t1,
            Transactional<T2, C> t2,
            Transactional<T3, C> t3
    ) {
        validateProviders(t1.provider, t2.provider, t3.provider);
        return new Transactional3<>(
                context -> Tuple.of(
                        t1.procedure.apply(context),
                        t2.procedure.apply(context),
                        t3.procedure.apply(context)
                ),
                t1.provider
        );
    }

    /**
     * A utility method for combining multiple {@code Transactional} results. The procedures are
     * executed sequentially. If any of the procedures throws, the transaction will be rolled
     * back.
     */
    public static <T1, T2, T3, T4, C> Transactional4<T1, T2, T3, T4, C> all(
            Transactional<T1, C> t1,
            Transactional<T2, C> t2,
            Transactional<T3, C> t3,
            Transactional<T4, C> t4
    ) {
        validateProviders(t1.provider, t2.provider, t3.provider, t4.provider);
        return new Transactional4<>(
                context -> Tuple.of(
                        t1.procedure.apply(context),
                        t2.procedure.apply(context),
                        t3.procedure.apply(context),
                        t4.procedure.apply(context)
                ),
                t1.provider
        );
    }

    /**
     * A utility method for combining multiple {@code Transactional} results. The procedures are
     * executed sequentially. If any of the procedures throws, the transaction will be rolled
     * back.
     */
    public static <T1, T2, T3, T4, T5, C> Transactional5<T1, T2, T3, T4, T5, C> all(
            Transactional<T1, C> t1,
            Transactional<T2, C> t2,
            Transactional<T3, C> t3,
            Transactional<T4, C> t4,
            Transactional<T5, C> t5
    ) {
        validateProviders(t1.provider, t2.provider, t3.provider, t4.provider, t5.provider);
        return new Transactional5<>(
                context -> Tuple.of(
                        t1.procedure.apply(context),
                        t2.procedure.apply(context),
                        t3.procedure.apply(context),
                        t4.procedure.apply(context),
                        t5.procedure.apply(context)
                ),
                t1.provider
        );
    }

    /**
     * A utility method for combining multiple {@code Transactional} results. The procedures are
     * executed sequentially. If any of the procedures throws, the transaction will be rolled
     * back.
     */
    public static <T1, T2, T3, T4, T5, T6, C> Transactional6<T1, T2, T3, T4, T5, T6, C> all(
            Transactional<T1, C> t1,
            Transactional<T2, C> t2,
            Transactional<T3, C> t3,
            Transactional<T4, C> t4,
            Transactional<T5, C> t5,
            Transactional<T6, C> t6
    ) {
        validateProviders(
                t1.provider,
                t2.provider,
                t3.provider,
                t4.provider,
                t5.provider,
                t6.provider
        );
        return new Transactional6<>(
                context -> Tuple.of(
                        t1.procedure.apply(context),
                        t2.procedure.apply(context),
                        t3.procedure.apply(context),
                        t4.procedure.apply(context),
                        t5.procedure.apply(context),
                        t6.procedure.apply(context)
                ),
                t1.provider
        );
    }

    /**
     * A utility method for combining multiple {@code Transactional} results. The procedures are
     * executed sequentially. If any of the procedures throws, the transaction will be rolled
     * back.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, C>
                    Transactional7<T1, T2, T3, T4, T5, T6, T7, C> all(
            Transactional<T1, C> t1,
            Transactional<T2, C> t2,
            Transactional<T3, C> t3,
            Transactional<T4, C> t4,
            Transactional<T5, C> t5,
            Transactional<T6, C> t6,
            Transactional<T7, C> t7
    ) {
        validateProviders(
                t1.provider,
                t2.provider,
                t3.provider,
                t4.provider,
                t5.provider,
                t6.provider,
                t7.provider
        );
        return new Transactional7<>(
                context -> Tuple.of(
                        t1.procedure.apply(context),
                        t2.procedure.apply(context),
                        t3.procedure.apply(context),
                        t4.procedure.apply(context),
                        t5.procedure.apply(context),
                        t6.procedure.apply(context),
                        t7.procedure.apply(context)
                ),
                t1.provider
        );
    }

    /**
     * A utility method for combining multiple {@code Transactional} results. The procedures are
     * executed sequentially. If any of the procedures throws, the transaction will be rolled
     * back.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, C>
                    Transactional8<T1, T2, T3, T4, T5, T6, T7, T8, C> all(
            Transactional<T1, C> t1,
            Transactional<T2, C> t2,
            Transactional<T3, C> t3,
            Transactional<T4, C> t4,
            Transactional<T5, C> t5,
            Transactional<T6, C> t6,
            Transactional<T7, C> t7,
            Transactional<T8, C> t8
    ) {
        validateProviders(
                t1.provider,
                t2.provider,
                t3.provider,
                t4.provider,
                t5.provider,
                t6.provider,
                t7.provider,
                t8.provider
        );
        return new Transactional8<>(
                context -> Tuple.of(
                        t1.procedure.apply(context),
                        t2.procedure.apply(context),
                        t3.procedure.apply(context),
                        t4.procedure.apply(context),
                        t5.procedure.apply(context),
                        t6.procedure.apply(context),
                        t7.procedure.apply(context),
                        t8.procedure.apply(context)
                ),
                t1.provider
        );
    }

    /**
     * A utility method for combining multiple {@code Transactional} results. The procedures are
     * executed sequentially. If any of the procedures throws, the transaction will be rolled
     * back.
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, C>
                    Transactional9<T0, T1, T2, T3, T4, T5, T6, T7, T8, C> all(
            Transactional<T0, C> t0,
            Transactional<T1, C> t1,
            Transactional<T2, C> t2,
            Transactional<T3, C> t3,
            Transactional<T4, C> t4,
            Transactional<T5, C> t5,
            Transactional<T6, C> t6,
            Transactional<T7, C> t7,
            Transactional<T8, C> t8
    ) {
        validateProviders(
                t0.provider,
                t1.provider,
                t2.provider,
                t3.provider,
                t4.provider,
                t5.provider,
                t6.provider,
                t7.provider,
                t8.provider
        );
        return new Transactional9<>(
                context -> Tuple.of(
                        t0.procedure.apply(context),
                        t1.procedure.apply(context),
                        t2.procedure.apply(context),
                        t3.procedure.apply(context),
                        t4.procedure.apply(context),
                        t5.procedure.apply(context),
                        t6.procedure.apply(context),
                        t7.procedure.apply(context),
                        t8.procedure.apply(context)
                ),
                t0.provider
        );
    }

    /**
     * A utility method for combining multiple {@code Transactional} results. The procedures are
     * executed sequentially. If any of the procedures throws, the transaction will be rolled
     * back.
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, C>
                    Transactional10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, C> all(
            Transactional<T0, C> t0,
            Transactional<T1, C> t1,
            Transactional<T2, C> t2,
            Transactional<T3, C> t3,
            Transactional<T4, C> t4,
            Transactional<T5, C> t5,
            Transactional<T6, C> t6,
            Transactional<T7, C> t7,
            Transactional<T8, C> t8,
            Transactional<T9, C> t9
    ) {
        validateProviders(
                t0.provider,
                t1.provider,
                t2.provider,
                t3.provider,
                t4.provider,
                t5.provider,
                t6.provider,
                t7.provider,
                t8.provider,
                t9.provider
        );
        return new Transactional10<>(
                context -> Tuple.of(
                        t0.procedure.apply(context),
                        t1.procedure.apply(context),
                        t2.procedure.apply(context),
                        t3.procedure.apply(context),
                        t4.procedure.apply(context),
                        t5.procedure.apply(context),
                        t6.procedure.apply(context),
                        t7.procedure.apply(context),
                        t8.procedure.apply(context),
                        t9.procedure.apply(context)
                ),
                t0.provider
        );
    }

    private static void validateProviders(Object... providers) {
        if (providers.length == 1) return;

        Object first = providers[0];
        for (Object other : providers) {
            if (Objects.equals(first, other)) continue;

            throw new IllegalArgumentException(
                    "Cannot create Transactional chain with multiple TransactionProviders"
            );
        }
    }
}
