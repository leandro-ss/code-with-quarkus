package br.com.quarkus.base.adapter;

import br.com.quarkus.domain.base.BaseEntity;

public interface SendAdapter<T extends BaseEntity> {

    void send(T object);

}
