package fi.jubic.easyutils.transactional;

import fi.jubic.easyutils.function.Consumer3;
import fi.jubic.easyutils.function.Function3;
import fi.jubic.easyutils.tuple.Tuple3;

import java.util.Objects;
import java.util.function.Function;

public class Transactional3<T0, T1, T2, C> extends Transactional<Tuple3<T0, T1, T2>, C> {
    Transactional3(
            Function<C, Tuple3<T0, T1, T2>> procedure,
            TransactionProvider<C> provider
    ) {
        super(procedure, provider);
    }


    public <U> Transactional<U, C> map(Function3<T0, T1, T2, U> mapper) {
        return super.map(tuple -> mapper.apply(tuple.get0(), tuple.get1(), tuple.get2()));
    }

    public <U> Transactional<U, C> flatMap(Function3<T0, T1, T2, Transactional<U, C>> mapper) {
        return super.flatMap(tuple -> mapper.apply(tuple.get0(), tuple.get1(), tuple.get2()));
    }

    public Transactional3<T0, T1, T2, C> peek(Consumer3<T0, T1, T2> action) {
        Objects.requireNonNull(action);
        return new Transactional3<>(
                context -> {
                    Tuple3<T0, T1, T2> value = procedure.apply(context);
                    action.accept(value.get0(), value.get1(), value.get2());
                    return value;
                },
                provider
        );
    }

    public Transactional3<T0, T1, T2, C> peekMap(
            Function3<T0, T1, T2, Transactional<Void, C>> procedure
    ) {
        Objects.requireNonNull(procedure);
        return new Transactional3<>(
                context -> {
                    Tuple3<T0, T1, T2> value = this.procedure.apply(context);
                    procedure.apply(value.get0(), value.get1(), value.get2())
                            .procedure
                            .apply(context);
                    return value;
                },
                provider
        );
    }
}
