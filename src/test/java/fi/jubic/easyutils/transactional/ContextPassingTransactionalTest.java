package fi.jubic.easyutils.transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ContextPassingTransactionalTest {
    private Ctx context;
    private CpProvider provider;

    @BeforeEach
    void beforeEach() {
        this.context = new Ctx();
        this.provider = spy(new CpProvider(this.context));
    }

    @Test
    void shouldPassProviderToMaterialize() {
        //noinspection unchecked
        Function<Ctx, Integer> materialize = (Function<Ctx, Integer>) mock(Function.class);
        when(materialize.apply(context)).thenReturn(10);

        assertEquals(
                Integer.valueOf(10),
                Transactional.of(materialize, provider).get()
        );

        verify(materialize).apply(context);
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldMapTransactionalValues() {
        //noinspection unchecked
        Function<Ctx, Integer> materialize = (Function<Ctx, Integer>) mock(Function.class);
        when(materialize.apply(context)).thenReturn(10);

        assertEquals(
                Integer.valueOf(15),
                Transactional.of(materialize, provider)
                        .map(integer -> integer + 5)
                        .get()
        );

        verify(materialize).apply(context);
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldFlatMapTransactionalValues() {
        //noinspection unchecked
        Function<Ctx, Integer> materialize1 = (Function<Ctx, Integer>) mock(Function.class);
        when(materialize1.apply(context)).thenReturn(10);
        //noinspection unchecked
        Function<Ctx, String> materialize2 = (Function<Ctx, String>) mock(Function.class);
        when(materialize2.apply(context)).thenReturn("str value");

        //noinspection unchecked
        Function<Integer, Transactional<String, Ctx>> flatMapper
                = (Function<Integer, Transactional<String, Ctx>>) mock(Function.class);
        when(flatMapper.apply(15)).thenReturn(Transactional.of(materialize2, provider));

        assertEquals(
                "str value",
                Transactional.of(materialize1, provider)
                        .map(integer -> integer + 5)
                        .flatMap(flatMapper)
                        .get()
        );

        verify(materialize1).apply(context);
        verify(materialize2).apply(context);
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldPeekTransactionalValues() {
        //noinspection unchecked
        Function<Ctx, Integer> materialize1 = (Function<Ctx, Integer>) mock(Function.class);
        when(materialize1.apply(context)).thenReturn(10);

        //noinspection unchecked
        Consumer<Integer> peek = (Consumer<Integer>) mock(Consumer.class);

        assertEquals(
                Integer.valueOf(10),
                Transactional.of(materialize1, provider)
                        .peek(peek)
                        .get()
        );

        verify(materialize1).apply(context);
        verify(peek).accept(eq(Integer.valueOf(10)));
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldPeekMapTransactionalValues() {
        //noinspection unchecked
        Function<Ctx, Integer> materialize1 = (Function<Ctx, Integer>) mock(Function.class);
        when(materialize1.apply(context)).thenReturn(10);
        //noinspection unchecked
        Function<Ctx, Void> materialize2 = (Function<Ctx, Void>) mock(Function.class);
        when(materialize2.apply(context)).thenReturn(null);

        //noinspection unchecked
        Function<Integer, Transactional<Void, Ctx>> peekMapper
                = (Function<Integer, Transactional<Void, Ctx>>) mock(Function.class);
        when(peekMapper.apply(10)).thenReturn(Transactional.of(materialize2, provider));

        assertEquals(
                Integer.valueOf(10),
                Transactional.of(materialize1, provider)
                        .peekMap(peekMapper)
                        .get()
        );

        verify(materialize1).apply(context);
        verify(materialize2).apply(context);
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldRunWithoutTransactionWhenBypassed() {
        //noinspection unchecked
        Function<Ctx, Integer> materialize = (Function<Ctx, Integer>) mock(Function.class);
        when(materialize.apply(context)).thenReturn(10);

        assertEquals(
                Integer.valueOf(10),
                Transactional.of(materialize, provider).bypass()
        );

        verify(materialize).apply(context);
        verify(provider, never()).runWithTransaction(any());
    }

    @Test
    void shouldThrowIfMaterializeThrows() {
        assertThrows(
                CustomException.class,
                () -> Transactional
                        .of(
                                ctx -> {
                                    throw new CustomException();
                                },
                                provider
                        )
                        .get()
        );
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldThrowIfMapperThrows() {
        assertThrows(
                CustomException.class,
                () -> Transactional.of(ctx -> 10, provider)
                        .map(val -> {
                            throw new CustomException();
                        })
                        .get()
        );

        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldThrowIfFlatMapperThrows() {
        assertThrows(
                CustomException.class,
                () -> Transactional.of(ctx -> 10, provider)
                        .flatMap(val -> {
                            throw new CustomException();
                        })
                        .get()
        );

        verify(provider, times(1)).runWithTransaction(any());
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

    private class CustomException extends RuntimeException {

    }
}
