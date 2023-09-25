package com.company;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Funciones {
    Checks check = new Checks();

    //ayuda al split a mantener los delimitadores
    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    //extrae comentarios
    public String ExtractComments(String code) {
        String[] CodewoComment = code.split("(//)[A-Za-z0-9\\s]+(//)");
        String complete = "", part;
        for (String s : CodewoComment) {
            part = s;
            complete += part;
        }
        return complete;
    }

    //imprime la tabla de id's
    public void printRWArray(ArrayList<Rw> arrayRW) {
        for (Rw i : arrayRW) {
            System.out.println("Reserved Word: "+i.word);
            System.out.println("scope: "+i.scope);
            System.out.println("Valor: "+i.valor);
            System.out.println("\n");
        }
    }

    //inicio entrega 1 ---------------------------------------------------------------------------------------------------
    //busqueda de id, retorna el id encontrado
    public Rw buscarRW(ArrayList<Rw> arrayRW, String _rw){
        for (Rw i:arrayRW) {
            if(i.word.equals(_rw)){
                return i;
            }
        }
        return null;
    }

    //al cerra un contexto, elimina las variables dentro del contexto cerrado
    public ArrayList<Rw> cerrarScope(ArrayList<Rw> arrayID, int _scope){
        for (int i = 0; i < arrayID.size(); i++) {
            if(arrayID.get(i).scope==_scope){
                arrayID.remove(i);
                i--;
            }
        }
        return arrayID;
    }

    //fin entrega 1 ---------------------------------------------------------------------------------------------------

    //metodo con validaciones para obtener los tokens, añade lexema y token a un arraylist
    public void secretaria(String srcCode) throws IOException {
    //VARIABLES
        //variables para el objeto id
        int scope = 0, valor,portnum;
        String RW, porcent=null,rw;
        //Arraylist de objetos id
        ArrayList<Rw> arrayRW = new ArrayList<>();
        String codeWOcomment, lineaSin, line;
        String[] Code_lines, lineArray;
        Hex hexa = new Hex();
        codeWOcomment = this.ExtractComments(srcCode);

        Code_lines = codeWOcomment.split("\n");

        //recorre el array del codigo fuente, cada elemento es una linea
        for (int i = 0; i < Code_lines.length; i++) {
            //reincio de valores con cada linea
            RW=null;
            valor=0;
            porcent=null;
            //System.out.println("\nLínea "+(i+1));
            lineaSin = "";
            line = Code_lines[i];

            lineArray = line.split(String.format(WITH_DELIMITER, "[|{} (),\t\s\r;=+*/-]"));

            //recorre cada elemento del array linea
            for (String s : lineArray) {
                if (check.esRW(s) != null) {
                    RW=s;
                    lineaSin += check.esRW(s);
                }else if (check.esNum(s) != null) {
                    valor= Integer.parseInt(s);
                    lineaSin += check.esNum(s);
                }else if(check.esMC(s)!= null){
                    //MC=s;
                    lineaSin += check.esMC(s);
                }else if (check.esSimb(s) != null) {
                    if (check.esSimb(s).equals("llaveAbierta ")) {
                        scope++;
                    } else if (check.esSimb(s).equals("llaveCerrada ")) {
                        //printRWArray(arrayRW);
                        arrayRW=cerrarScope(arrayRW,scope);
                        scope--;
                    } else {
                        lineaSin += check.esSimb(s);
                    }
                }
                else if(s.matches("^%[0|1]{8}$")){
                    String[] ports =s.split("%");
                    porcent= ports[1].trim() ;
                }
            }

            //validar creacion o modificacion de id
            if (RW != null) {

                if (porcent!=null){
                    portnum=7;
                    String[] valores = porcent.split(String.format("(?<=\\G.{%d})",1));
                    for (String val:valores) {
                        rw = String.format(RW+".%d",portnum);
                        Rw objRw = new Rw(rw, scope);
                        arrayRW.add(objRw);
                        objRw.setValor(Integer.parseInt(val));
                        portnum--;
                    }
                }else{
                    Rw objRw = new Rw(RW, scope);
                    arrayRW.add(objRw);
                    objRw.setValor(valor);
                }

            }

            //System.out.println(lineaSin);
            this.getAnalisisSin(lineaSin, i + 1);
        }

        check.scope(scope);
       // printRWArray(arrayRW);
        hexa.setHex(arrayRW);
        System.out.println("Analisis terminado");
    }

    //compara cada linea con las reglas del analizador sintactico
    public void getAnalisisSin(String line, int nLine) {
        Diccionarios table = new Diccionarios();
        ArrayList<String> rules = table.getTableRules();
        boolean valido = false;
        for (String r : rules) {
            if (Objects.equals(line, r)) {
                valido = true;
                break;
            }
        }
        if (!valido) {
            System.out.println("error sintáctico, Línea " + nLine);
        }
    }
}
