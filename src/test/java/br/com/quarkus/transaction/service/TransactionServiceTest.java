package br.com.quarkus.transaction.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.redisson.api.RLiveObjectService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.inject.Inject;

import br.com.quarkus.domain.BankingTransaction;
import br.com.quarkus.domain.TransactionData;
import br.com.quarkus.transaction.legacy.LegacyAdapterMainFrame;

import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;


import static br.com.quarkus.domain.base.TransactionType.INTERNET_BANKING;
import static io.quarkus.test.junit.QuarkusMock.installMockForType;
import static java.time.LocalDateTime.of;
import static org.mockito.Mockito.when;

/**
 * Teste bem basico mesmo, por falta de tempo
 * Sei lá pq nao ta injetando o Mock
 */
@QuarkusTest
public class TransactionServiceTest {

    private static final long CLIENT_KEY = 1L;
    private static final BigDecimal TRANSACTION_VALUE = BigDecimal.TEN;
    private static final LocalDateTime TRANSACTION_DATE_TIME = of(2020, 6, 20, 0, 0);
    @InjectMock
    private TransactionService transactionService;
    private static RLiveObjectService rLiveObjectService;
    private static LegacyAdapterMainFrame legacyAdapterMainFrame;
    private BankingTransaction bankingTransaction;

    @Captor
    private ArgumentCaptor<TransactionData> transactionDataArgumentCaptor;

    @BeforeAll
    public static void setup() {
        rLiveObjectService = Mockito.mock(RLiveObjectService.class);
        legacyAdapterMainFrame = Mockito.mock(LegacyAdapterMainFrame.class);
        installMockForType(legacyAdapterMainFrame, LegacyAdapterMainFrame.class);
        installMockForType(rLiveObjectService, RLiveObjectService.class);
    }

    @Test
    void name() {
        dadoUmaTransacao();
        dadoUmaTransacaoEmProcessamento();
        quandoAdicionarTransacao();
        deveConstarTotalAmbasTransacoes();
    }

    private void deveConstarTotalAmbasTransacoes() {
        Mockito.verify(legacyAdapterMainFrame).send(transactionDataArgumentCaptor.capture());

        BigDecimal ammount = transactionDataArgumentCaptor.getValue().getBankingTransactionSet().stream()
            .map(BankingTransaction::getTransactionValue)
            .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        Assertions.assertEquals(BigDecimal.valueOf(20), ammount);
    }

    private void dadoUmaTransacaoEmProcessamento() {
        when(rLiveObjectService.get(TransactionData.class, bankingTransaction.getClientKey()))
            .thenAnswer(a -> new TransactionData(new BankingTransaction(CLIENT_KEY, TRANSACTION_VALUE, INTERNET_BANKING, TRANSACTION_DATE_TIME)));
    }

    private void quandoAdicionarTransacao() {
        transactionService.addBankingTransaction(bankingTransaction);
    }

    private void dadoUmaTransacao() {
        bankingTransaction = new BankingTransaction(CLIENT_KEY, TRANSACTION_VALUE, INTERNET_BANKING, TRANSACTION_DATE_TIME);
    }
}