package br.com.quarkus.transaction.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.quarkus.domain.BankingTransaction;
import br.com.quarkus.base.exceptions.NotFoundException;
import br.com.quarkus.transaction.service.TransactionService;


/**
 * Disponivel somente para testes
 */
@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BankingTransactionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankingTransactionController.class);

    private final TransactionService transactionService;

    public BankingTransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GET @Path("/{key}")
    public BankingTransaction findById(@PathParam("key") Integer key) {
        LOGGER.debug("findById-id:{}", key);
        return transactionService.findById(key).orElseThrow(NotFoundException::new);
    }

    @DELETE @Path("/{key}")
    public void deleteById(@PathParam("key") Integer key) {
        LOGGER.debug("deleteById-key:{}", key);
        transactionService.deleteClienKey(key);
    }

    @POST @Path("/addTransactionBanking")
    public void addTransaction(BankingTransaction bankingTransaction) {
        LOGGER.debug("save-task:{}", bankingTransaction);
        transactionService.addBankingTransaction(bankingTransaction);
    }
}
