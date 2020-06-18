package br.com.quarkus.transaction.service.usecase;


import java.util.Optional;
import java.util.StringJoiner;

import br.com.quarkus.domain.TransactionData;
import br.com.quarkus.domain.BankingTransaction;
import br.com.quarkus.base.usecase.UseCase;

public class MergeTransactionUseCase implements UseCase<TransactionData> {

    private final BankingTransaction transactionBanking;
    private final Optional<TransactionData> recentTransaction;

    public MergeTransactionUseCase(BankingTransaction transactionBanking,
                                   Optional<TransactionData> recentTransaction) {
        this.transactionBanking = transactionBanking;
        this.recentTransaction = recentTransaction;
    }

    public MergeTransactionUseCase execute() {
        recentTransaction.ifPresent(this::merge);
        return this;
    }

    public Optional<TransactionData> result(){
        TransactionData transactionData = recentTransaction.orElseGet(() -> new TransactionData(transactionBanking));
        return Optional.of(transactionData);
    }

    private void merge(TransactionData transactionData){
        transactionData.addBankingTransaction(transactionBanking);
    }

    public boolean hasRecentTransaction(){
        return recentTransaction.isPresent();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MergeTransactionUseCase.class.getSimpleName() + "[", "]")
            .add("transactionBanking=" + transactionBanking)
            .add("recentTransaction=" + recentTransaction)
            .toString();
    }
}
