package com.company;

public class Rw {
    String word, device;
    int scope, valor;

    public Rw(String _word, int _scope){
        this.word =_word;
        this.scope=_scope;
    }

    public void setValor(int _valor){
        this.valor =_valor;
    }
    public void setDevice(String _device){
        this.device=_device;
    }

}