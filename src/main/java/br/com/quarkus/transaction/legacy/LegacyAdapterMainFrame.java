package br.com.quarkus.transaction.legacy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

import br.com.quarkus.base.adapter.SendAdapter;
import br.com.quarkus.domain.TransactionData;

/**
 * Forma de comunicacao com o legado provavelmente por Socket,
 * De qualquer forma ainda devendo ser implementado
 */
@ApplicationScoped
public class LegacyAdapterMainFrame implements SendAdapter<TransactionData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyAdapterMainFrame.class);

    public void send(TransactionData transactionData){
        LOGGER.debug("send - transactionData:{}",transactionData);
    }
}
