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

class ThreadContextTransactionalTest {
    private TcProvider provider;

    @BeforeEach
    void beforeEach() {
        this.provider = spy(new TcProvider());
    }

    @Test
    void shouldPassProviderToMaterialize() {
        assertEquals(
                Integer.valueOf(10),
                Transactional.of(() -> 10, provider).get()
        );

        verify(provider, times(1)).begin();
        verify(provider, times(1)).commit();
        verify(provider, never()).rollback();
    }

    @Test
    void shouldMapTransactionalValues() {
        assertEquals(
                Integer.valueOf(15),
                Transactional.of(() -> 10, provider)
                        .map(integer -> integer + 5)
                        .get()
        );

        verify(provider, times(1)).begin();
        verify(provider, times(1)).commit();
        verify(provider, never()).rollback();
    }

    @Test
    void shouldFlatMapTransactionalValues() {
        //noinspection unchecked
        Function<Integer, Transactional<String, Void>> flatMapper
                = (Function<Integer, Transactional<String, Void>>) mock(Function.class);
        when(flatMapper.apply(eq(15))).thenReturn(Transactional.of(() -> "str value", provider));

        assertEquals(
                "str value",
                Transactional.of(() -> 10, provider)
                        .map(integer -> integer + 5)
                        .flatMap(flatMapper)
                        .get()
        );

        verify(provider, times(1)).begin();
        verify(provider, times(1)).commit();
        verify(provider, never()).rollback();
    }

    @Test
    void shouldPeekTransactionalValues() {
        //noinspection unchecked
        Consumer<Integer> peek = (Consumer<Integer>) mock(Consumer.class);

        assertEquals(
                Integer.valueOf(10),
                Transactional.of(() -> 10, provider)
                        .peek(peek)
                        .get()
        );

        verify(peek).accept(eq(Integer.valueOf(10)));
        verify(provider, times(1)).runWithTransaction(any());
    }

    @Test
    void shouldPeekMapTransactionalValues() {
        //noinspection unchecked
        Function<Integer, Transactional<Void, Void>> peekMapper
                = (Function<Integer, Transactional<Void, Void>>) mock(Function.class);
        when(peekMapper.apply(10)).thenReturn(Transactional.of(() -> null, provider));

        assertEquals(
                Integer.valueOf(10),
                Transactional.of(() -> 10, provider)
                        .peekMap(peekMapper)
                        .get()
        );

        verify(provider, times(1)).begin();
        verify(provider, times(1)).commit();
        verify(provider, never()).rollback();
    }

    @Test
    void shouldRunWithoutTransactionWhenBypassed() {
        assertEquals(
                Integer.valueOf(10),
                Transactional.of(() -> 10, provider).bypass()
        );

        verify(provider, never()).begin();
        verify(provider, never()).commit();
        verify(provider, never()).rollback();
    }

    @Test
    void shouldThrowIfMaterializeThrows() {
        assertThrows(
                CustomException.class,
                () -> Transactional
                        .of(
                                () -> {
                                    throw new CustomException();
                                },
                                provider
                        )
                        .get()
        );

        verify(provider, times(1)).begin();
        verify(provider, never()).commit();
        verify(provider, times(1)).rollback();
    }

    @Test
    void shouldThrowIfMapperThrows() {
        assertThrows(
                CustomException.class,
                () -> Transactional.of(() -> 10, provider)
                        .map(integer -> {
                            throw new CustomException();
                        })
                        .get()
        );

        verify(provider, times(1)).begin();
        verify(provider, never()).commit();
        verify(provider, times(1)).rollback();
    }

    @Test
    void shouldThrowIfFlatMapperThrows() {
        assertThrows(
                CustomException.class,
                () -> Transactional.of(() -> 10, provider)
                        .flatMap(integer -> {
                            throw new CustomException();
                        })
                        .get()
        );

        verify(provider, times(1)).begin();
        verify(provider, never()).commit();
        verify(provider, times(1)).rollback();
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

    class CustomException extends RuntimeException {

    }
}
