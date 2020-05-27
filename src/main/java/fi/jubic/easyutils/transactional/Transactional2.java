package fi.jubic.easyutils.transactional;

import fi.jubic.easyutils.tuple.Tuple2;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Transactional2<T0, T1, C> extends Transactional<Tuple2<T0, T1>, C> {
    Transactional2(
            Function<C, Tuple2<T0, T1>> procedure,
            TransactionProvider<C> provider
    ) {
        super(procedure, provider);
    }


    public <U> Transactional<U, C> map(BiFunction<T0, T1, U> mapper) {
        return super.map(tuple -> mapper.apply(tuple.get0(), tuple.get1()));
    }

    public <U> Transactional<U, C> flatMap(BiFunction<T0, T1, Transactional<U, C>> mapper) {
        return super.flatMap(tuple -> mapper.apply(tuple.get0(), tuple.get1()));
    }

    public Transactional2<T0, T1, C> peek(BiConsumer<T0, T1> action) {
        Objects.requireNonNull(action);
        return new Transactional2<>(
                context -> {
                    Tuple2<T0, T1> tuple = procedure.apply(context);
                    action.accept(tuple.get0(), tuple.get1());
                    return tuple;
                },
                provider
        );
    }

    public Transactional<Tuple2<T0, T1>, C> peekMap(
            BiFunction<T0, T1, Transactional<Void, C>> procedure
    ) {
        Objects.requireNonNull(procedure);
        return new Transactional2<>(
                context -> {
                    Tuple2<T0, T1> tuple = this.procedure.apply(context);
                    procedure.apply(tuple.get0(), tuple.get1())
                            .procedure
                            .apply(context);
                    return tuple;
                },
                provider
        );
    }
}
