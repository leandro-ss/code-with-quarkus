package br.com.quarkus.transaction.service;

import org.redisson.api.RLiveObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.quarkus.domain.TransactionData;
import br.com.quarkus.domain.BankingTransaction;
import br.com.quarkus.transaction.legacy.LegacyAdapterMainFrame;
import br.com.quarkus.transaction.service.usecase.MergeTransactionUseCase;

import static java.util.Optional.ofNullable;


/**
 * Classe principal resposanvel por sincronizar os dados e enviar para o legado
 * ainda com uma ideia de rascunho, mas já usavel - useCase precisa ser evoluido pra facilitar o testes
 * Rotina de callback a partir do legado seria responsavel por remover dados de sincronia
 *
 * Ideia eh basicamente ter alertas para possiveis processos em andamento
 *
 * Alem de ser ideia base para base de consulta a estar disponivel a partir dos insumos do legado
 */
@Transactional
@ApplicationScoped
public class TransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    private final RLiveObjectService rLiveObjectService;
    private final LegacyAdapterMainFrame legacyAdapterMainFrame;

    @Inject
    public TransactionService(RLiveObjectService rLiveObjectService, LegacyAdapterMainFrame legacyAdapterMainFrame) {
        this.rLiveObjectService = rLiveObjectService;
        this.legacyAdapterMainFrame = legacyAdapterMainFrame;
    }

    public Optional<BankingTransaction> findById(Integer clientKey) {
        return ofNullable(rLiveObjectService.get(BankingTransaction.class, clientKey));
    }

    public void addBankingTransaction(BankingTransaction bankingTransaction) {
        Optional<TransactionData> recentTransaction = ofNullable(rLiveObjectService.get(TransactionData.class, bankingTransaction.getClientKey()));

        recentTransaction.ifPresent(t -> LOGGER.debug("recentTransaction - {}", t));

        MergeTransactionUseCase mergeTransactionUseCase = new MergeTransactionUseCase(bankingTransaction, recentTransaction).execute();

        LOGGER.debug("mergeTransactionUseCase - {}", mergeTransactionUseCase);

        if (mergeTransactionUseCase.hasRecentTransaction()) {
            mergeTransactionUseCase.result().ifPresent( proxied -> {
                var transactionData = rLiveObjectService.detach(proxied);
                legacyAdapterMainFrame.send(transactionData);
            });
        } else {
            mergeTransactionUseCase.result().ifPresent( transactionData -> {
                rLiveObjectService.persist(transactionData);
                legacyAdapterMainFrame.send(transactionData);
            });
        }
    }

    public void deleteClienKey(Integer clientKey) {
        this.rLiveObjectService.delete(BankingTransaction.class, clientKey);
    }
}
