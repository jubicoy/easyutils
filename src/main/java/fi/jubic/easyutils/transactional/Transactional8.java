package fi.jubic.easyutils.transactional;

import fi.jubic.easyutils.function.Consumer8;
import fi.jubic.easyutils.function.Function8;
import fi.jubic.easyutils.tuple.Tuple8;

import java.util.Objects;
import java.util.function.Function;

public class Transactional8<T0, T1, T2, T3, T4, T5, T6, T7, C>
        extends Transactional<Tuple8<T0, T1, T2, T3, T4, T5, T6, T7>, C> {
    Transactional8(
            Function<C, Tuple8<T0, T1, T2, T3, T4, T5, T6, T7>> procedure,
            TransactionProvider<C> provider
    ) {
        super(procedure, provider);
    }


    public <U> Transactional<U, C> map(Function8<T0, T1, T2, T3, T4, T5, T6, T7, U> mapper) {
        return super.map(
                tuple -> mapper.apply(
                        tuple.get0(),
                        tuple.get1(),
                        tuple.get2(),
                        tuple.get3(),
                        tuple.get4(),
                        tuple.get5(),
                        tuple.get6(),
                        tuple.get7()
                )
        );
    }

    public <U> Transactional<U, C> flatMap(
            Function8<T0, T1, T2, T3, T4, T5, T6, T7, Transactional<U, C>> mapper
    ) {
        return super.flatMap(
                tuple -> mapper.apply(
                        tuple.get0(),
                        tuple.get1(),
                        tuple.get2(),
                        tuple.get3(),
                        tuple.get4(),
                        tuple.get5(),
                        tuple.get6(),
                        tuple.get7()
                )
        );
    }

    public Transactional8<T0, T1, T2, T3, T4, T5, T6, T7, C> peek(
            Consumer8<T0, T1, T2, T3, T4, T5, T6, T7> action
    ) {
        Objects.requireNonNull(action);
        return new Transactional8<>(
                context -> {
                    Tuple8<T0, T1, T2, T3, T4, T5, T6, T7> tuple = procedure.apply(context);
                    action.accept(
                            tuple.get0(),
                            tuple.get1(),
                            tuple.get2(),
                            tuple.get3(),
                            tuple.get4(),
                            tuple.get5(),
                            tuple.get6(),
                            tuple.get7()
                    );
                    return tuple;
                },
                provider
        );
    }

    public Transactional8<T0, T1, T2, T3, T4, T5, T6, T7, C> peekMap(
            Function8<T0, T1, T2, T3, T4, T5, T6, T7, Transactional<Void, C>> procedure
    ) {
        Objects.requireNonNull(procedure);
        return new Transactional8<>(
                context -> {
                    Tuple8<T0, T1, T2, T3, T4, T5, T6, T7> tuple = this.procedure.apply(context);
                    procedure
                            .apply(
                                    tuple.get0(),
                                    tuple.get1(),
                                    tuple.get2(),
                                    tuple.get3(),
                                    tuple.get4(),
                                    tuple.get5(),
                                    tuple.get6(),
                                    tuple.get7()
                            )
                            .procedure
                            .apply(context);
                    return tuple;
                },
                provider
        );
    }
}
