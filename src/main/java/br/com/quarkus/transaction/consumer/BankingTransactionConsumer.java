package br.com.quarkus.transaction.consumer;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.quarkus.domain.BankingTransaction;
import br.com.quarkus.transaction.service.TransactionService;

@ApplicationScoped
public class BankingTransactionConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankingTransactionConsumer.class);

    private final TransactionService transactionService;

    public BankingTransactionConsumer(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Incoming("incoming-transaction")
    public void incomingTransaction(BankingTransaction bankingTransaction) {
        LOGGER.debug("incomingTransaction:{}", bankingTransaction);
        transactionService.addBankingTransaction(bankingTransaction);
    }
}
