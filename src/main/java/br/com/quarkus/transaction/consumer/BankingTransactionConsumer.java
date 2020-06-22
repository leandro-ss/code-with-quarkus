package br.com.quarkus.transaction.consumer;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.quarkus.domain.BankingTransaction;
import br.com.quarkus.transaction.service.TransactionService;

/**
 * Consumer com uma ideia bem simplista, precisando evoluir muito ainda
 * a principio nao deve ter problemas de concorrencia
 *
 * O ideal é ter ainda Streams do kafka q pegaria a informacao de varios topicos possivelmente
 * um por Workflow, para realizar a padronizacao dos dados e enriquecimento se necessario de outras fontes
 */
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
