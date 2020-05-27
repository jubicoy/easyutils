package fi.jubic.easyutils.transactional;

import fi.jubic.easyutils.function.Consumer4;
import fi.jubic.easyutils.function.Function4;
import fi.jubic.easyutils.tuple.Tuple4;

import java.util.Objects;
import java.util.function.Function;

public class Transactional4<T0, T1, T2, T3, C> extends Transactional<Tuple4<T0, T1, T2, T3>, C> {
    Transactional4(
            Function<C, Tuple4<T0, T1, T2, T3>> procedure,
            TransactionProvider<C> provider
    ) {
        super(procedure, provider);
    }


    public <U> Transactional<U, C> map(Function4<T0, T1, T2, T3, U> mapper) {
        return super.map(
                tuple -> mapper.apply(tuple.get0(), tuple.get1(), tuple.get2(), tuple.get3())
        );
    }

    public <U> Transactional<U, C> flatMap(Function4<T0, T1, T2, T3, Transactional<U, C>> mapper) {
        return super.flatMap(
                tuple -> mapper.apply(tuple.get0(), tuple.get1(), tuple.get2(), tuple.get3())
        );
    }

    public Transactional4<T0, T1, T2, T3, C> peek(Consumer4<T0, T1, T2, T3> action) {
        Objects.requireNonNull(action);
        return new Transactional4<>(
                context -> {
                    Tuple4<T0, T1, T2, T3> tuple = procedure.apply(context);
                    action.accept(tuple.get0(), tuple.get1(), tuple.get2(), tuple.get3());
                    return tuple;
                },
                provider
        );
    }

    public Transactional4<T0, T1, T2, T3, C> peekMap(
            Function4<T0, T1, T2, T3, Transactional<Void, C>> procedure
    ) {
        Objects.requireNonNull(procedure);
        return new Transactional4<>(
                context -> {
                    Tuple4<T0, T1, T2, T3> tuple = this.procedure.apply(context);
                    procedure.apply(tuple.get0(), tuple.get1(), tuple.get2(), tuple.get3())
                            .procedure
                            .apply(context);
                    return tuple;
                },
                provider
        );
    }
}
