package fi.jubic.easyutils.transactional;

import fi.jubic.easyutils.function.Consumer7;
import fi.jubic.easyutils.function.Function7;
import fi.jubic.easyutils.tuple.Tuple7;

import java.util.Objects;
import java.util.function.Function;

public class Transactional7<T0, T1, T2, T3, T4, T5, T6, C>
        extends Transactional<Tuple7<T0, T1, T2, T3, T4, T5, T6>, C> {
    Transactional7(
            Function<C, Tuple7<T0, T1, T2, T3, T4, T5, T6>> procedure,
            TransactionProvider<C> provider
    ) {
        super(procedure, provider);
    }


    public <U> Transactional<U, C> map(Function7<T0, T1, T2, T3, T4, T5, T6, U> mapper) {
        return super.map(
                tuple -> mapper.apply(
                        tuple.get0(),
                        tuple.get1(),
                        tuple.get2(),
                        tuple.get3(),
                        tuple.get4(),
                        tuple.get5(),
                        tuple.get6()
                )
        );
    }

    public <U> Transactional<U, C> flatMap(
            Function7<T0, T1, T2, T3, T4, T5, T6, Transactional<U, C>> mapper
    ) {
        return super.flatMap(
                tuple -> mapper.apply(
                        tuple.get0(),
                        tuple.get1(),
                        tuple.get2(),
                        tuple.get3(),
                        tuple.get4(),
                        tuple.get5(),
                        tuple.get6()
                )
        );
    }

    public Transactional7<T0, T1, T2, T3, T4, T5, T6, C> peek(
            Consumer7<T0, T1, T2, T3, T4, T5, T6> action
    ) {
        Objects.requireNonNull(action);
        return new Transactional7<>(
                context -> {
                    Tuple7<T0, T1, T2, T3, T4, T5, T6> tuple = procedure.apply(context);
                    action.accept(
                            tuple.get0(),
                            tuple.get1(),
                            tuple.get2(),
                            tuple.get3(),
                            tuple.get4(),
                            tuple.get5(),
                            tuple.get6()
                    );
                    return tuple;
                },
                provider
        );
    }

    public Transactional7<T0, T1, T2, T3, T4, T5, T6, C> peekMap(
            Function7<T0, T1, T2, T3, T4, T5, T6, Transactional<Void, C>> procedure
    ) {
        Objects.requireNonNull(procedure);
        return new Transactional7<>(
                context -> {
                    Tuple7<T0, T1, T2, T3, T4, T5, T6> tuple = this.procedure.apply(context);
                    procedure
                            .apply(
                                    tuple.get0(),
                                    tuple.get1(),
                                    tuple.get2(),
                                    tuple.get3(),
                                    tuple.get4(),
                                    tuple.get5(),
                                    tuple.get6()
                            )
                            .procedure
                            .apply(context);
                    return tuple;
                },
                provider
        );
    }
}
