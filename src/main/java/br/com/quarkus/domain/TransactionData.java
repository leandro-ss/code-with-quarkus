package br.com.quarkus.domain;

import com.google.common.collect.Sets;

import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import br.com.quarkus.domain.base.BaseEntity;

/**
 * Classe Responsavel armazaenar a representação da transação do cliente munida de informações e regras de negocio
 * da area de prevencao de fraudes, atuando também como um Wrappper, o dado estaria dispónviel de forma
 * efemera através de um TTL a ser configorado no redis, sendo passivel de consulta/atualizacao por qualquer outra instancia
 *
 * Ideia bem a nivel de rascunho
 */
@REntity
public class TransactionData implements BaseEntity {
    private static final long serialVersionUID = 1L;

    @RId
    private Long clientKey;
    private Set<BankingTransaction> bankingTransactionSet = Sets.newHashSet();

    public TransactionData() {
    }

    public TransactionData(@Nonnull BankingTransaction bankingTransactionSet) {
        this.clientKey = bankingTransactionSet.getClientKey();
        this.bankingTransactionSet.add(bankingTransactionSet);
    }

    public Long getClientKey() {
        return clientKey;
    }

    public void setClientKey(Long clientKey) {
        this.clientKey = clientKey;
    }

    public List<BankingTransaction> getBankingTransactionSet() {
        return List.copyOf(bankingTransactionSet);
    }

    public void addBankingTransaction(BankingTransaction bankingTransaction) {
        if(this.clientKey.equals(bankingTransaction.getClientKey())){
            this.bankingTransactionSet.add(bankingTransaction);
        }
    }

    public void removeBankingTransaction(BankingTransaction bankingTransaction) {
        this.bankingTransactionSet.remove(bankingTransaction);
    }

    public void setBankingTransactionSet(Set<BankingTransaction> bankingTransactionSet) {
        this.bankingTransactionSet = bankingTransactionSet;
    }
}