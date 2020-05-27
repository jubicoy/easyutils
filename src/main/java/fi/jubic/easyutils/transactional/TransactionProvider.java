package fi.jubic.easyutils.transactional;

import java.util.function.Function;

/**
 * A provider interface for running 3rd party transactions within the {@link Transactional}
 * framework.
 *
 * @param <C> the type of the transaction context
 */
public interface TransactionProvider<C> {
    /**
     * Begin a transaction that is committed when the procedure exists or rolled back if the
     * procedure throws an {@link RuntimeException}. The value thrown by the procedure should be
     * rethrown.
     *
     * @param procedure the procedure to run within a transaction returning a result
     * @param <T> the type of the returned result
     * @return the result of the procedure
     */
    <T> T runWithTransaction(Function<C, T> procedure);

    /**
     * Acquire the result without wrapping the operation in a transaction. It is possible to define
     * data access strictly in terms of {@code Transactional}s without forcing all interactions to
     * be wrapped in transactions.
     *
     * <p>
     *     A {@code TransactionProvider} implementation is not required to provide support for
     *     non-transactional procedures. In these cases the implementation should throw an
     *     {@link UnsupportedOperationException}.
     * </p>
     *
     * @param procedure the procedure to run with a non-transactional context
     * @param <T> the type of the returned result
     * @return the result of the procedure
     */
    <T> T runWithoutTransaction(Function<C, T> procedure);
}
