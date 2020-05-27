# easyutils

A collection of utility classes.

## `Transactional`

Think of `java.util.Optional` and transactional procedures. A sequence of interconnected operations can be performed in a transactional context without explicitly dealing with transactions.

Here's a contrived example. `recordTransaction` returns a `Transactional` wrapping a procedure for adding (or subtracting) amounts to the balance of the given account in context of `Ctx` transaction context. `transferFunds` utilises `Transactional::flatMap` to chain the operations. When `Transactional::get` is called at the end of the chain, the two procedures are performed in a transaction scope and the result of the last procedure is returned.

```java
/**
 * A method for adding monetary transactions to a log.
 */
Transactional<Account, Ctx> recordTransaction(UUID accountId, BigDecimal amount);

/**
 * Transfer funds from one account to another transactionally.
 */
void transferFunds(UUID from, UUID to, BigDecimal amount) {
    recordTransaction(from, amount.negate())
        .flatMap(ignore -> recordTransaction(to, amount))
        .get();
}
```

The `ignore` parameter to `Transactional::flatMap` makes you wish for a way to perform multiple operations at once without them depending directly on each other. For this use there is the `Transactional::all` method that supports performing multiple operations sequentially.

```java
void transferFunds(UUID from, UUID to, BigDecimal amount) {
    Transactiona.all(
        recordTransaction(from, amount.negate()),
        recordTransaction(to, amount)
    ).get();
}
```

Most of the functionality relies on the `TransactionProvider` that is responsible for providing the transaction scope. There are two flavors of providers:

* Vanilla `TransactionProvider` offers `TransactionProvider::runWithTransaction` and `TransactionProvider::runWithoutTransaction`. This works well together with JOOQ's `DSLContext::transactionResult`.
* `ThreadContextTransactionProvider` can be used with contextual API that has the more common `begin`, `commit` and `rollback` methods working together with the context of the running thread.
