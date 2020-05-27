package fi.jubic.easyutils.transactional;

import fi.jubic.easyutils.function.Consumer9;
import fi.jubic.easyutils.function.Function9;
import fi.jubic.easyutils.tuple.Tuple9;

import java.util.Objects;
import java.util.function.Function;

public class Transactional9<T0, T1, T2, T3, T4, T5, T6, T7, T8, C>
        extends Transactional<Tuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8>, C> {
    Transactional9(
            Function<C, Tuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8>> procedure,
            TransactionProvider<C> provider
    ) {
        super(procedure, provider);
    }


    public <U> Transactional<U, C> map(Function9<T0, T1, T2, T3, T4, T5, T6, T7, T8, U> mapper) {
        return super.map(
                tuple -> mapper.apply(
                        tuple.get0(),
                        tuple.get1(),
                        tuple.get2(),
                        tuple.get3(),
                        tuple.get4(),
                        tuple.get5(),
                        tuple.get6(),
                        tuple.get7(),
                        tuple.get8()
                )
        );
    }

    public <U> Transactional<U, C> flatMap(
            Function9<T0, T1, T2, T3, T4, T5, T6, T7, T8, Transactional<U, C>> mapper
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
                        tuple.get7(),
                        tuple.get8()
                )
        );
    }

    public Transactional9<T0, T1, T2, T3, T4, T5, T6, T7, T8, C> peek(
            Consumer9<T0, T1, T2, T3, T4, T5, T6, T7, T8> action
    ) {
        Objects.requireNonNull(action);
        return new Transactional9<>(
                context -> {
                    Tuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> tuple = procedure.apply(context);
                    action.accept(
                            tuple.get0(),
                            tuple.get1(),
                            tuple.get2(),
                            tuple.get3(),
                            tuple.get4(),
                            tuple.get5(),
                            tuple.get6(),
                            tuple.get7(),
                            tuple.get8()
                    );
                    return tuple;
                },
                provider
        );
    }

    public Transactional9<T0, T1, T2, T3, T4, T5, T6, T7, T8, C> peekMap(
            Function9<T0, T1, T2, T3, T4, T5, T6, T7, T8, Transactional<Void, C>> procedure
    ) {
        Objects.requireNonNull(procedure);
        return new Transactional9<>(
                context -> {
                    Tuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> tuple
                            = this.procedure.apply(context);
                    procedure
                            .apply(
                                    tuple.get0(),
                                    tuple.get1(),
                                    tuple.get2(),
                                    tuple.get3(),
                                    tuple.get4(),
                                    tuple.get5(),
                                    tuple.get6(),
                                    tuple.get7(),
                                    tuple.get8()
                            )
                            .procedure
                            .apply(context);
                    return tuple;
                },
                provider
        );
    }
}
