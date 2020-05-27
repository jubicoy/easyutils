package fi.jubic.easyutils.transactional;

import fi.jubic.easyutils.function.Consumer5;
import fi.jubic.easyutils.function.Function5;
import fi.jubic.easyutils.tuple.Tuple5;

import java.util.Objects;
import java.util.function.Function;

public class Transactional5<T0, T1, T2, T3, T4, C>
        extends Transactional<Tuple5<T0, T1, T2, T3, T4>, C> {
    Transactional5(
            Function<C, Tuple5<T0, T1, T2, T3, T4>> procedure,
            TransactionProvider<C> provider
    ) {
        super(procedure, provider);
    }


    public <U> Transactional<U, C> map(Function5<T0, T1, T2, T3, T4, U> mapper) {
        return super.map(
                tuple -> mapper.apply(
                        tuple.get0(),
                        tuple.get1(),
                        tuple.get2(),
                        tuple.get3(),
                        tuple.get4()
                )
        );
    }

    public <U> Transactional<U, C> flatMap(
            Function5<T0, T1, T2, T3, T4, Transactional<U, C>> mapper
    ) {
        return super.flatMap(
                tuple -> mapper.apply(
                        tuple.get0(),
                        tuple.get1(),
                        tuple.get2(),
                        tuple.get3(),
                        tuple.get4()
                )
        );
    }

    public Transactional5<T0, T1, T2, T3, T4, C> peek(Consumer5<T0, T1, T2, T3, T4> action) {
        Objects.requireNonNull(action);
        return new Transactional5<>(
                context -> {
                    Tuple5<T0, T1, T2, T3, T4> tuple = procedure.apply(context);
                    action.accept(
                            tuple.get0(),
                            tuple.get1(),
                            tuple.get2(),
                            tuple.get3(),
                            tuple.get4()
                    );
                    return tuple;
                },
                provider
        );
    }

    public Transactional5<T0, T1, T2, T3, T4, C> peekMap(
            Function5<T0, T1, T2, T3, T4, Transactional<Void, C>> procedure
    ) {
        Objects.requireNonNull(procedure);
        return new Transactional5<>(
                context -> {
                    Tuple5<T0, T1, T2, T3, T4> tuple = this.procedure.apply(context);
                    procedure
                            .apply(
                                    tuple.get0(),
                                    tuple.get1(),
                                    tuple.get2(),
                                    tuple.get3(),
                                    tuple.get4()
                            )
                            .procedure
                            .apply(context);
                    return tuple;
                },
                provider
        );
    }
}
