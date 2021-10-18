package br.com.sousa.coopervote.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public abstract class Entidade implements Serializable {

    protected String id;

    protected Entidade(){
        this.id = UUID.randomUUID().toString();
    }

}
