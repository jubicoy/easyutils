package fi.jubic.easyutils.transactional;

import fi.jubic.easyutils.tuple.Tuple10;
import fi.jubic.easyutils.tuple.Tuple2;
import fi.jubic.easyutils.tuple.Tuple3;
import fi.jubic.easyutils.tuple.Tuple4;
import fi.jubic.easyutils.tuple.Tuple5;
import fi.jubic.easyutils.tuple.Tuple6;
import fi.jubic.easyutils.tuple.Tuple7;
import fi.jubic.easyutils.tuple.Tuple8;
import fi.jubic.easyutils.tuple.Tuple9;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AllTransactionalTest {
    private CpProvider provider;
    private TcProvider tcProvider;

    @BeforeEach
    void beforeEach() {
        this.provider = spy(new CpProvider(new Ctx()));
        this.tcProvider = spy(new TcProvider());
    }

    @Test
    void shouldConstructTuple2() {
        Tuple2<Integer, String> result = Transactional
                .all(
                        Transactional.of(ctx -> 10, provider),
                        Transactional.of(ctx -> "text", provider)
                )
                .get();

        assertEquals(Integer.valueOf(10), result.get0());
        assertEquals("text", result.get1());
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldConstructTuple3() {
        Tuple3<Integer, String, Float> result = Transactional
                .all(
                        Transactional.of(ctx -> 10, provider),
                        Transactional.of(ctx -> "text", provider),
                        Transactional.of(ctx -> 1.0f, provider)
                )
                .get();

        assertEquals(Integer.valueOf(10), result.get0());
        assertEquals("text", result.get1());
        assertEquals(Float.valueOf(1.0f), result.get2());
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldConstructTuple4() {
        Tuple4<Integer, String, Float, Boolean> result = Transactional
                .all(
                        Transactional.of(ctx -> 10, provider),
                        Transactional.of(ctx -> "text", provider),
                        Transactional.of(ctx -> 1.0f, provider),
                        Transactional.of(ctx -> true, provider)
                )
                .get();

        assertEquals(Integer.valueOf(10), result.get0());
        assertEquals("text", result.get1());
        assertEquals(Float.valueOf(1.0f), result.get2());
        assertEquals(Boolean.TRUE, result.get3());
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldConstructTuple5() {
        Tuple5<Integer, String, Float, Boolean, Double> result = Transactional
                .all(
                        Transactional.of(ctx -> 10, provider),
                        Transactional.of(ctx -> "text", provider),
                        Transactional.of(ctx -> 1.0f, provider),
                        Transactional.of(ctx -> true, provider),
                        Transactional.of(ctx -> 2.0, provider)
                )
                .get();

        assertEquals(Integer.valueOf(10), result.get0());
        assertEquals("text", result.get1());
        assertEquals(Float.valueOf(1.0f), result.get2());
        assertEquals(Boolean.TRUE, result.get3());
        assertEquals(Double.valueOf(2.0), result.get4());
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldConstructTuple6() {
        Tuple6<Integer, String, Float, Boolean, Double, Long> result = Transactional
                .all(
                        Transactional.of(ctx -> 10, provider),
                        Transactional.of(ctx -> "text", provider),
                        Transactional.of(ctx -> 1.0f, provider),
                        Transactional.of(ctx -> true, provider),
                        Transactional.of(ctx -> 2.0, provider),
                        Transactional.of(ctx -> 5L, provider)
                )
                .get();

        assertEquals(Integer.valueOf(10), result.get0());
        assertEquals("text", result.get1());
        assertEquals(Float.valueOf(1.0f), result.get2());
        assertEquals(Boolean.TRUE, result.get3());
        assertEquals(Double.valueOf(2.0), result.get4());
        assertEquals(Long.valueOf(5L), result.get5());
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldConstructTuple7() {
        Tuple7<Integer, String, Float, Boolean, Double, Long, Short> result = Transactional
                .all(
                        Transactional.of(ctx -> 10, provider),
                        Transactional.of(ctx -> "text", provider),
                        Transactional.of(ctx -> 1.0f, provider),
                        Transactional.of(ctx -> true, provider),
                        Transactional.of(ctx -> 2.0, provider),
                        Transactional.of(ctx -> 5L, provider),
                        Transactional.of(ctx -> (short)6, provider)
                )
                .get();

        assertEquals(Integer.valueOf(10), result.get0());
        assertEquals("text", result.get1());
        assertEquals(Float.valueOf(1.0f), result.get2());
        assertEquals(Boolean.TRUE, result.get3());
        assertEquals(Double.valueOf(2.0), result.get4());
        assertEquals(Long.valueOf(5L), result.get5());
        assertEquals(Short.valueOf((short)6), result.get6());
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldConstructTuple8() {
        Tuple8<Integer, String, Float, Boolean, Double, Long, Short, Byte> result = Transactional
                .all(
                        Transactional.of(ctx -> 10, provider),
                        Transactional.of(ctx -> "text", provider),
                        Transactional.of(ctx -> 1.0f, provider),
                        Transactional.of(ctx -> true, provider),
                        Transactional.of(ctx -> 2.0, provider),
                        Transactional.of(ctx -> 5L, provider),
                        Transactional.of(ctx -> (short)6, provider),
                        Transactional.of(ctx -> (byte) 3, provider)
                )
                .get();

        assertEquals(Integer.valueOf(10), result.get0());
        assertEquals("text", result.get1());
        assertEquals(Float.valueOf(1.0f), result.get2());
        assertEquals(Boolean.TRUE, result.get3());
        assertEquals(Double.valueOf(2.0), result.get4());
        assertEquals(Long.valueOf(5L), result.get5());
        assertEquals(Short.valueOf((short)6), result.get6());
        assertEquals(Byte.valueOf((byte)3), result.get7());
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldConstructTuple9() {
        Tuple9<
                Integer,
                String,
                Float,
                Boolean,
                Double,
                Long,
                Short,
                Byte,
                Character
                > result = Transactional
                .all(
                        Transactional.of(ctx -> 10, provider),
                        Transactional.of(ctx -> "text", provider),
                        Transactional.of(ctx -> 1.0f, provider),
                        Transactional.of(ctx -> true, provider),
                        Transactional.of(ctx -> 2.0, provider),
                        Transactional.of(ctx -> 5L, provider),
                        Transactional.of(ctx -> (short)6, provider),
                        Transactional.of(ctx -> (byte) 3, provider),
                        Transactional.of(ctx -> 'X', provider)
                )
                .get();

        assertEquals(Integer.valueOf(10), result.get0());
        assertEquals("text", result.get1());
        assertEquals(Float.valueOf(1.0f), result.get2());
        assertEquals(Boolean.TRUE, result.get3());
        assertEquals(Double.valueOf(2.0), result.get4());
        assertEquals(Long.valueOf(5L), result.get5());
        assertEquals(Short.valueOf((short)6), result.get6());
        assertEquals(Byte.valueOf((byte)3), result.get7());
        assertEquals(Character.valueOf('X'), result.get8());
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldConstructTuple10() {
        Tuple10<
                        Integer,
                        String,
                        Float,
                        Boolean,
                        Double,
                        Long,
                        Short,
                        Byte,
                        Character,
                        List<String>
                        > result = Transactional
                .all(
                        Transactional.of(ctx -> 10, provider),
                        Transactional.of(ctx -> "text", provider),
                        Transactional.of(ctx -> 1.0f, provider),
                        Transactional.of(ctx -> true, provider),
                        Transactional.of(ctx -> 2.0, provider),
                        Transactional.of(ctx -> 5L, provider),
                        Transactional.of(ctx -> (short)6, provider),
                        Transactional.of(ctx -> (byte) 3, provider),
                        Transactional.of(ctx -> 'X', provider),
                        Transactional.of(ctx -> Arrays.asList("a", "b"), provider)
                )
                .get();

        assertEquals(Integer.valueOf(10), result.get0());
        assertEquals("text", result.get1());
        assertEquals(Float.valueOf(1.0f), result.get2());
        assertEquals(Boolean.TRUE, result.get3());
        assertEquals(Double.valueOf(2.0), result.get4());
        assertEquals(Long.valueOf(5L), result.get5());
        assertEquals(Short.valueOf((short)6), result.get6());
        assertEquals(Byte.valueOf((byte)3), result.get7());
        assertEquals(Character.valueOf('X'), result.get8());
        assertEquals(Arrays.asList("a", "b"), result.get9());
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldMapAllConstructedTuple() {
        assertEquals(
                Integer.valueOf(120),
                Transactional
                        .all(
                                Transactional.of(ctx -> 10, provider),
                                Transactional.of(ctx -> 80, provider),
                                Transactional.of(ctx -> 25, provider),
                                Transactional.of(ctx -> 5, provider)
                        )
                        .map(result -> result.get0()
                                + result.get1()
                                + result.get2()
                                + result.get3()
                        )
                        .get()
        );
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldMapTransactional2() {
        //noinspection unchecked
        BiFunction<Integer, String, Object> mapper
                = (BiFunction<Integer, String, Object>) mock(BiFunction.class);
        Object returnVal = new Object();
        when(mapper.apply(any(), any())).thenReturn(returnVal);

        assertSame(
                returnVal,
                Transactional
                        .all(
                                Transactional.of(ctx -> 10, provider),
                                Transactional.of(ctx -> "text", provider)
                        )
                        .map(mapper)
                        .get()
        );

        verify(mapper, times(1)).apply(
                eq(10),
                eq("text")
        );
    }

    @Test
    void shouldNotAllowMixingTransactionContexts() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Transactional.all(
                        Transactional.of(ctx -> 10, provider),
                        Transactional.of(ctx -> 15, new CpProvider(new Ctx()))
                )
        );
    }

    @Test
    void shouldThrowIfAnyThrows() {
        assertThrows(
                CustomException.class,
                () -> Transactional
                        .all(
                                Transactional.of(ctx -> 1, provider),
                                Transactional.of(ctx -> 2, provider),
                                Transactional.of(ctx -> 3, provider),
                                Transactional.of(
                                        ctx -> {
                                            throw new CustomException();
                                        },
                                        provider
                                ),
                                Transactional.of(ctx -> 5, provider),
                                Transactional.of(ctx -> 6, provider)
                        )
                        .get()
        );
    }

    @Test
    void shouldInvokeOnlyASingleThreadContextTransaction() {
        Transactional
                .all(
                        Transactional.of(ctx -> 1, tcProvider),
                        Transactional.of(ctx -> 2, tcProvider),
                        Transactional.of(ctx -> 3, tcProvider),
                        Transactional.of(ctx -> 4, tcProvider),
                        Transactional.of(ctx -> 5, tcProvider),
                        Transactional.of(ctx -> 6, tcProvider),
                        Transactional.of(ctx -> 7, tcProvider)
                )
                .get();

        verify(tcProvider, times(1)).begin();
        verify(tcProvider, times(1)).commit();
        verify(tcProvider, never()).rollback();
    }

    @Test
    void shouldRollbackThreadContextTransactionOnException() {
        assertThrows(
                CustomException.class,
                () -> Transactional
                        .all(
                                Transactional.of(ctx -> 1, tcProvider),
                                Transactional.of(ctx -> 2, tcProvider),
                                Transactional.of(ctx -> 3, tcProvider),
                                Transactional.of(ctx -> 4, tcProvider),
                                Transactional.of(ctx -> 5, tcProvider),
                                Transactional.of(
                                        ctx -> {
                                            throw new CustomException();
                                        },
                                        tcProvider
                                ),
                                Transactional.of(ctx -> 7, tcProvider),
                                Transactional.of(ctx -> 8, tcProvider)
                        )
                        .get()
        );

        verify(tcProvider, times(1)).begin();
        verify(tcProvider, never()).commit();
        verify(tcProvider, times(1)).rollback();
    }

    class Ctx {

    }

    class CpProvider implements TransactionProvider<Ctx> {
        private final Ctx context;

        CpProvider(Ctx context) {
            this.context = context;
        }

        @Override
        public <T> T runWithTransaction(Function<Ctx, T> procedure) {
            return procedure.apply(context);
        }

        @Override
        public <T> T runWithoutTransaction(Function<Ctx, T> procedure) {
            return procedure.apply(context);
        }
    }

    class TcProvider implements ThreadContextTransactionProvider {
        @Override
        public void begin() {

        }

        @Override
        public void commit() {

        }

        @Override
        public void rollback() {

        }
    }

    private class CustomException extends RuntimeException {

    }
}
