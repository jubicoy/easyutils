package fi.jubic.easyutils.transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.function.Function;


/**
 * A {@link TransactionProvider} for thread context based implementations. A void transaction
 * context is passed to the procedures and external
 * {@link ThreadContextTransactionProvider#begin()},
 * {@link ThreadContextTransactionProvider#commit()} and
 * {@link ThreadContextTransactionProvider#rollback()} methods are called to manage the transaction
 * scope.
 */
@SuppressFBWarnings("THROWS_METHOD_THROWS_RUNTIMEEXCEPTION")
public interface ThreadContextTransactionProvider extends TransactionProvider<Void> {
    /**
     * Begin a thread context bound transaction.
     */
    void begin();

    /**
     * Commit current thread context bound transaction.
     */
    void commit();

    /**
     * Rollback current thread context bound transaction.
     */
    void rollback();

    @Override
    default <T> T runWithTransaction(Function<Void, T> procedure) {
        begin();
        try {
            T result = procedure.apply(null);
            commit();
            return result;
        }
        catch (RuntimeException exception) {
            rollback();
            throw exception;
        }
    }

    @Override
    default <T> T runWithoutTransaction(Function<Void, T> procedure) {
        return procedure.apply(null);
    }
}
