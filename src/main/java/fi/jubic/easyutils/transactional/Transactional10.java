package fi.jubic.easyutils.transactional;

import fi.jubic.easyutils.function.Consumer10;
import fi.jubic.easyutils.function.Function10;
import fi.jubic.easyutils.tuple.Tuple10;

import java.util.Objects;
import java.util.function.Function;

public class Transactional10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, C>
        extends Transactional<Tuple10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>, C> {
    Transactional10(
            Function<C, Tuple10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>> procedure,
            TransactionProvider<C> provider
    ) {
        super(procedure, provider);
    }


    public <U> Transactional<U, C> map(
            Function10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, U> mapper
    ) {
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
                        tuple.get8(),
                        tuple.get9()
                )
        );
    }

    public <U> Transactional<U, C> flatMap(
            Function10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, Transactional<U, C>> mapper
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
                        tuple.get8(),
                        tuple.get9()
                )
        );
    }

    public Transactional10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, C> peek(
            Consumer10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> action
    ) {
        Objects.requireNonNull(action);
        return new Transactional10<>(
                context -> {
                    Tuple10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> tuple
                            = procedure.apply(context);
                    action.accept(
                            tuple.get0(),
                            tuple.get1(),
                            tuple.get2(),
                            tuple.get3(),
                            tuple.get4(),
                            tuple.get5(),
                            tuple.get6(),
                            tuple.get7(),
                            tuple.get8(),
                            tuple.get9()
                    );
                    return tuple;
                },
                provider
        );
    }

    public Transactional10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, C> peekMap(
            Function10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, Transactional<Void, C>> procedure
    ) {
        Objects.requireNonNull(procedure);
        return new Transactional10<>(
                context -> {
                    Tuple10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> tuple
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
                                    tuple.get8(),
                                    tuple.get9()
                            )
                            .procedure
                            .apply(context);
                    return tuple;
                },
                provider
        );
    }
}
