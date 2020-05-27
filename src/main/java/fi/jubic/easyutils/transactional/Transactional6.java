package fi.jubic.easyutils.transactional;

import fi.jubic.easyutils.function.Consumer6;
import fi.jubic.easyutils.function.Function6;
import fi.jubic.easyutils.tuple.Tuple6;

import java.util.Objects;
import java.util.function.Function;

public class Transactional6<T0, T1, T2, T3, T4, T5, C>
        extends Transactional<Tuple6<T0, T1, T2, T3, T4, T5>, C> {
    Transactional6(
            Function<C, Tuple6<T0, T1, T2, T3, T4, T5>> procedure,
            TransactionProvider<C> provider
    ) {
        super(procedure, provider);
    }


    public <U> Transactional<U, C> map(Function6<T0, T1, T2, T3, T4, T5, U> mapper) {
        return super.map(
                tuple -> mapper.apply(
                        tuple.get0(),
                        tuple.get1(),
                        tuple.get2(),
                        tuple.get3(),
                        tuple.get4(),
                        tuple.get5()
                )
        );
    }

    public <U> Transactional<U, C> flatMap(
            Function6<T0, T1, T2, T3, T4, T5, Transactional<U, C>> mapper
    ) {
        return super.flatMap(
                tuple -> mapper.apply(
                        tuple.get0(),
                        tuple.get1(),
                        tuple.get2(),
                        tuple.get3(),
                        tuple.get4(),
                        tuple.get5()
                )
        );
    }

    public Transactional6<T0, T1, T2, T3, T4, T5, C> peek(
            Consumer6<T0, T1, T2, T3, T4, T5> action
    ) {
        Objects.requireNonNull(action);
        return new Transactional6<>(
                context -> {
                    Tuple6<T0, T1, T2, T3, T4, T5> tuple = procedure.apply(context);
                    action.accept(
                            tuple.get0(),
                            tuple.get1(),
                            tuple.get2(),
                            tuple.get3(),
                            tuple.get4(),
                            tuple.get5()
                    );
                    return tuple;
                },
                provider
        );
    }

    public Transactional6<T0, T1, T2, T3, T4, T5, C> peekMap(
            Function6<T0, T1, T2, T3, T4, T5, Transactional<Void, C>> procedure
    ) {
        Objects.requireNonNull(procedure);
        return new Transactional6<>(
                context -> {
                    Tuple6<T0, T1, T2, T3, T4, T5> tuple = this.procedure.apply(context);
                    procedure
                            .apply(
                                    tuple.get0(),
                                    tuple.get1(),
                                    tuple.get2(),
                                    tuple.get3(),
                                    tuple.get4(),
                                    tuple.get5()
                            )
                            .procedure
                            .apply(context);
                    return tuple;
                },
                provider
        );
    }
}
