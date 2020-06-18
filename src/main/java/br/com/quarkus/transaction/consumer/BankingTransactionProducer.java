package br.com.quarkus.transaction.consumer;

import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;

import br.com.quarkus.transaction.controller.BankingTransactionController;
import io.reactivex.Flowable;

/**
 * Disponivel somente para testes
 */
@ApplicationScoped
public class BankingTransactionProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankingTransactionController.class);

    private final Random random ;

    public BankingTransactionProducer() {
        this.random = new Random();
    }

    @Outgoing("outgoing-prices")
    public Flowable<Integer> generate() {
        LOGGER.debug("generate");
        return Flowable.interval(5, TimeUnit.SECONDS)
            .map(tick -> random.nextInt(100));
    }
}
