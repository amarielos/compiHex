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
        rulesArray.add("RW asig num fin ");
    //DEVICE 16F628A;
        rulesArray.add("RW MC fin ");
        return rulesArray;
    }
}