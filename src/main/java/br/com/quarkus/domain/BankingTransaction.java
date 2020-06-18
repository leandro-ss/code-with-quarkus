package br.com.quarkus.domain;

import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.quarkus.domain.base.BaseEntity;
import br.com.quarkus.domain.base.TransactionType;


/**
 * Classe Responsavel por representar os dados vindo de um dos canais disponiveis pelo cliente
 */
public class BankingTransaction implements BaseEntity {
    private static final long serialVersionUID = 1L;

    private UUID transactionKey;

    private Long clientKey;
    private BigDecimal transactionValue;
    private TransactionType transactionType;
    private LocalDateTime transactionDateTime;

    public BankingTransaction(Long clientKey,
                              BigDecimal transactionValue,
                              TransactionType transactionType,
                              LocalDateTime transactionDateTime) {
        this.clientKey = clientKey;
        this.transactionValue = transactionValue;
        this.transactionType = transactionType;
        this.transactionDateTime = transactionDateTime;
        this.transactionKey = UUID.randomUUID();
    }

    public Long getClientKey() {
        return clientKey;
    }

    public void setClientKey(Long clientKey) {
        this.clientKey = clientKey;
    }

    public BigDecimal getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(BigDecimal transactionValue) {
        this.transactionValue = transactionValue;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(LocalDateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BankingTransaction.class.getSimpleName() + "[", "]")
            .add("clientKey=" + clientKey)
            .add("transactionValue=" + transactionValue)
            .add("transactionType=" + transactionType)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingTransaction that = (BankingTransaction) o;
        return Objects.equal(transactionKey, that.transactionKey);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(transactionKey);
    }
}

