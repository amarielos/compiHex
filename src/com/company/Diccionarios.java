package com.company;
import java.util.ArrayList;

public class Diccionarios {

    public ArrayList<String> getTableRW(){
        ArrayList<String> rwArray = new ArrayList<>();
        rwArray.add("if");
        rwArray.add("TRISA");
        rwArray.add("TRISB");
        rwArray.add("DEVICE");
        rwArray.add("Device");
        rwArray.add("PORTA");
        rwArray.add("PORTB");

        rwArray.add("PORTA.0");
        rwArray.add("PORTA.1");
        rwArray.add("PORTA.2");
        rwArray.add("PORTA.3");
        rwArray.add("PORTA.4");
        rwArray.add("PORTA.5");
        rwArray.add("PORTA.6");
        rwArray.add("PORTA.7");

        rwArray.add("PORTB.0");
        rwArray.add("PORTB.1");
        rwArray.add("PORTB.2");
        rwArray.add("PORTB.3");
        rwArray.add("PORTB.4");
        rwArray.add("PORTB.5");
        rwArray.add("PORTB.6");
        rwArray.add("PORTB.7");

        rwArray.add("inicio:");
        rwArray.add("GoTo");

        rwArray.add("DelayMS");

        return rwArray;
    }

    public ArrayList<String> getTableSimb(){
        ArrayList<String> simbArray =new ArrayList<>();
        simbArray.add("+ suma");
        simbArray.add("- dif");
        simbArray.add("* multi");
        simbArray.add("/ div");
        simbArray.add("= asig");
        simbArray.add("; fin");
        simbArray.add(", sep");
        simbArray.add("( parAbierto");
        simbArray.add(") parCerrado");
        simbArray.add("{ llaveAbierta");
        simbArray.add("} llaveCerrada");
        simbArray.add("| or");
        return simbArray;
    }

    public ArrayList<String> getTableRules(){
        ArrayList<String> rulesArray = new ArrayList<>();
        rulesArray.add("");
        rulesArray.add(" ");
        rulesArray.add("\n");
    //TRISA = 0;
        rulesArray.add("RW asig num ");
    //DEVICE 16F628A;
        rulesArray.add("RW MC ");
    //DEVICE 16F628A;
        rulesArray.add("RW RW ");
    //DEVICE 16F628A;
        rulesArray.add("RW ");
    //DelayMS 100;
        rulesArray.add("RW time ");

        return rulesArray;
    }
}