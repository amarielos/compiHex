package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Hex {
    Funciones fun = new Funciones();

    public String es2dig(int cont){
        String contador=Integer.toHexString(cont).toUpperCase();
        if(contador.length()<2){
            contador="0"+contador;
        }
        return contador;
    }

    public String getAcumulador(String hex){
        int decimal = 256;
        int num;
        String[] suma = hex.split("(?<=\\G.{"+2+"})");
        for (String elemento:suma) {
            num = Integer.parseInt(elemento,16);
            decimal-= num;
            if(decimal<0){
                decimal+=256;
            }
        }
        return es2dig(decimal);
    }

    public String getPrimerLinea(String direccion, String datos, int cont, int cont2){
        String hex;
        String contador = es2dig(cont);
        String contador2 = "00"+es2dig(cont2)+"00";
        String acumulador=getAcumulador(contador+contador2+direccion+datos);
        hex = ":"+contador+contador2+direccion+datos+acumulador;
        return hex;
    }

    public String getLinea(String datos, int cont, int cont2){
        String hex;
        String contador = es2dig(cont);
        String contador2 = "00"+es2dig(cont2)+"00";
        String acumulador=getAcumulador(contador+contador2+datos);
        hex = ":"+contador+contador2+datos+acumulador;
        return hex;
    }

    public String portLetraPar(String word){
        if(word.equals("PORTA")){
            word="05";
        }else{
            word="06";
        }
        return word;
    }

    public String portLetraImpar(String word){
        if(word.equals("PORTA")){
            word="85";
        }else{
            word="86";
        }
        return word;
    }

    public String portValor0(String num){
        if(num.matches("[01]")){
            num = "10";
        }else if(num.matches("[23]")){
            num = "11";
        }else if(num.matches("[45]")){
            num = "12";
        }else if(num.matches("[67]")){
            num = "13";
        }
        return num;
    }

    public String portValor1(String num){
        if(num.matches("[01]")){
            num = "14";
        }else if(num.matches("[23]")){
            num = "15";
        }else if(num.matches("[45]")){
            num = "16";
        }else if(num.matches("[67]")){
            num = "17";
        }
        return num;
    }

    public void setHex(ArrayList<Rw> array){
        Rw var;
        boolean esPrimerTris=true,esPrimerPort=true, lineaCompleta=false;
        int cont=0, cont2=0, inicio=1;
        String datos="",dato,ultLinea="\n"+":02400E00223F4F\n"+":00000001FF";
        String direccion="", hex="",primerlinea="",delay="";
        if(fun.buscarRW(array,"DelayMS")!=null){
            delay=  ":100000002828A301A200FF30A207031CA307031C9A\n" +
                    ":1000100023280330A100DF300F200328A101E83E90\n" +
                    ":10002000A000A109FC30031C1828A00703181528FC\n" +
                    ":10003000A0070000A10F152820181E28A01C2228A8\n" +
                    ":100040000000222808008313831203130000080015\n";
            cont2=80;
            inicio=40;
        }else{
            direccion="0128";
            cont=2;
        }

        for (Rw word:array) {
            if(word.word.matches("TRIS[A|B]")){
                if(word.valor==1){
                    dato="0130";
                    datos+=dato;
                    cont+=2;
                    inicio++;
                }
                if(esPrimerTris){
                    dato="8316";
                    datos+=dato;
                    cont+=2;
                    esPrimerTris=false;
                    inicio++;
                }
                if(word.word.equals("TRISA")){
                    dato="85";
                    datos+=dato;
                    cont++;
                }else if(word.word.equals("TRISB")){
                    dato="86";
                    datos+=dato;
                    cont++;
                }
                if(word.valor==1){
                    dato="00";
                    datos+=dato;
                    cont++;
                }else if(word.valor==0){
                    dato="01";
                    datos+=dato;
                    cont++;
                }
                inicio++;
            }else if(word.word.matches("PORT[AB]\\.?[0-7]?")){
                if(word.valor==1){
                    dato="0130";
                    datos+=dato;
                    cont+=2;
                    inicio++;
                }
                if(esPrimerPort){
                    if(!esPrimerTris){
                        dato="8312";
                        datos+=dato;
                        cont+=2;
                        esPrimerPort=false;
                        inicio++;
                    }
                }
                if(word.word.matches("PORT[AB]\\.[0-7]")){
                    String[] portArray = word.word.split("\\.");
                    if(portArray[1].matches("[0|2|4|6]")){
                        dato=portLetraPar(portArray[0]);
                    }else{
                        dato=portLetraImpar(portArray[0]);
                    }
                    datos+=dato;
                    cont++;
                    if (word.valor==0){
                        dato=portValor0(portArray[1]);
                    }else{
                        dato=portValor1(portArray[1]);
                    }
                    datos+=dato;
                    cont++;
                }else{
                    dato=portLetraImpar(word.word);
                    datos+=dato;
                    cont++;
                    if (word.valor==0){
                        dato="01";
                    }else{
                        dato="00";
                    }
                    datos+=dato;
                    cont++;
                }
                inicio++;
            }else if(word.word.equals("inicio:")){
                if(esPrimerPort){
                    if(!esPrimerTris){
                        dato="8312";
                        datos+=dato;
                        cont+=2;
                        esPrimerPort=false;
                        inicio++;
                    }
                }
                var= fun.buscarRW(array,"GoTo");
                var.setValor(inicio);
            }else if(word.word.equals("GoTo")){
                dato = es2dig(word.valor)+"28";
                datos+=dato;
                cont+=2;
            }else if(word.word.equals("DelayMS")){
                dato = es2dig(word.valor)+"300120";
                datos+=dato;
                cont+=4;
                inicio+=2;
            }
            if (cont>=16){
                if(primerlinea.equals("")){
                    primerlinea=getPrimerLinea(direccion,datos,cont,cont2);
                    hex=primerlinea;
                }else{
                    hex+="\n"+getLinea(datos,cont,cont2);
                }
                lineaCompleta=true;
                datos="";
                cont=0;
                cont2+=16;

            }else{
                lineaCompleta=false;
            }
        }
        if (!lineaCompleta){
            if(primerlinea.equals("")){
                hex=getPrimerLinea(direccion,datos,cont,cont2);
            }else{
                hex+="\n"+getLinea(datos,cont,cont2);
            }
        }
        compilar(delay+hex+ultLinea);
    }

    public void compilar(String hex){
        try {
            FileWriter fw = new FileWriter("hex/hex.hex");
            fw.write(hex);
            fw.close();
        } catch (IOException e) {
            System.out.println("Ruta no encontrada");
        }
    }
}
