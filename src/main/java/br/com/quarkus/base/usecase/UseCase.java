package br.com.quarkus.base.usecase;

import java.util.Optional;

import br.com.quarkus.domain.base.BaseEntity;

public interface UseCase<T extends BaseEntity> {

    UseCase<T> execute();

    default Optional<T> result(){
        return Optional.empty();
    }
}
