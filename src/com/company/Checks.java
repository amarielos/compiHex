package com.company;
import java.util.ArrayList;
import java.util.Objects;

public class Checks {
    //Aqui se hacen todas las comprobaciones entre el codigo fuente y los diccionarios

    // METODO, RECIBE COMO PARÁMETRO UNA PALABRA Y VERIFICA SI ES RW
    public String esRW(String word){
        Diccionarios table = new Diccionarios();
        ArrayList<String> tableArray = table.getTableRW();
        for (String s : tableArray) {
            if (Objects.equals(word, s)) {
                return "RW ";
            }
        }return null;
    }

    public String esMC(String word){
        if (word.matches("^([a-zA-Z\\d_$])([a-zA-Z\\d_$]*)$")){
            return "MC ";
        }
        return null;
    }

    // METODO, RECIBE COMO PARÁMETRO UNA PALABRA Y VERIFICA SI ES UN NUMERO (int o decimal)
    public String esNum(String word){
        if (word.matches("^[01]")){
            return "num ";
        }
        return null;
    }

    //METODO, RECIBE PALABRA Y REGRESA SI ES UN SIMBOLO (1 CARACTER)
    public String esSimb(String word){
        Diccionarios table = new Diccionarios();
        ArrayList<String> simbArray = table.getTableSimb();
        String[] SimbDesc;

        for (String s : simbArray) {
            SimbDesc = s.split(" ");
            if (word.equals(SimbDesc[0])){
                return  SimbDesc[1]+" ";
            }
        }
        return null;
    }

    //verifica error de missing brace
    public void scope(int scope){
        if(scope==0){
            System.out.println("");
        }else if(scope>0){
            System.out.println("ERROR: '}' expected");
        }else{
            System.out.println("ERROR: '{' expected");
        }
    }



}

