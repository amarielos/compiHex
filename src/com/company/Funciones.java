package com.company;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Funciones {
    Checks check = new Checks();

    //ayuda al split a mantener los delimitadores
    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    //
    public String getAcumulador(String hex){
        int decimal = 256;
        int num;
        String[] suma = hex.split("(?<=\\G.{"+2+"})");
        for (String elemento:suma) {
            if(decimal<0){
                decimal+=256;
            }
            num = Integer.parseInt(elemento,16);
            decimal-= num;
        }
        return Integer.toHexString(decimal).toUpperCase();
    }
    public String es2dig(int cont){
        String contador=Integer.toHexString(cont).toUpperCase();
        if(contador.length()<2){
            contador="0"+contador;
        }
        return contador;

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
        String contador2 = "00"+cont2+"00";
        String acumulador=getAcumulador(contador+contador2+datos);
        hex = ":"+contador+contador2+datos+acumulador;
        return hex;
    }
    public void setHex(ArrayList<Rw> array){
        boolean esPrimerTris=true, multiLinea=false;
        int cont=2, cont2=0;
        String acumulador="", contador="",contador2="",datos="",ultLinea="\n"+":02400E00223F4F\n"+":00000001FF";
        String direccion = "0128", hex="",primerlinea="";

        for (Rw word:array) {
            if(word.word.matches("TRIS[A|B]")){
                if(word.valor==1){
                    datos+="0130";
                    cont+=2;
                }
                if(esPrimerTris){
                    datos+="8316";
                    cont+=2;
                    esPrimerTris=false;
                }
                if(word.word.equals("TRISA")){
                    datos+="85";
                    cont++;
                }else if(word.word.equals("TRISB")){
                    datos+="86";
                    cont++;
                }
                if(word.valor==1){
                    datos+="00";
                    cont++;
                }else if(word.valor==0){
                    datos+="01";
                    cont++;
                }
            }
            if (cont>16){
                if(primerlinea.equals("")){
                    primerlinea=getPrimerLinea(direccion,datos,cont,cont2);
                    hex=primerlinea+"\n";
                    datos="";
                }
                multiLinea=true;
                cont=0;
                cont2+=10;
                hex+=getLinea(datos,cont,cont2);
            }
        }
        if (!multiLinea){
            hex=getPrimerLinea(direccion,datos,cont,cont2);
        }
        compilar(hex+ultLinea);
    }

    public void compilar(String hex){
        try {
            FileWriter fw = new FileWriter("hex/hex.txt");
            fw.write(hex);
            fw.close();
        } catch (IOException e) {
            System.out.println("Ruta no encontrada");
        }
    }

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
        int scope = 0, valor;
        String RW,MC;
        //Arraylist de objetos id
        ArrayList<Rw> arrayRW = new ArrayList<>();
        String codeWOcomment, lineaSin, line;
        String[] Code_lines, lineArray;

        codeWOcomment = this.ExtractComments(srcCode);

        Code_lines = codeWOcomment.split("\n");

        //recorre el array del codigo fuente, cada elemento es una linea
        for (int i = 0; i < Code_lines.length; i++) {
            //reincio de valores con cada linea
            RW=null;
            valor=0;
            //MC=null;

            //System.out.println("\nLínea "+(i+1));
            lineaSin = "";
            line = Code_lines[i];

            lineArray = line.split(String.format(WITH_DELIMITER, "[|{} (),\t\s;=+*/-]"));

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
                       // printIDArray(arrayId);
                        arrayRW=cerrarScope(arrayRW,scope);
                        scope--;
                    } else {
                        lineaSin += check.esSimb(s);
                    }
                }
            }

            //validar creacion o modificacion de id
            if (RW != null) {
                Rw objRw = new Rw(RW, scope);
                arrayRW.add(objRw);
                objRw.setValor(valor);
            }

            //System.out.println(lineaSin);
            this.getAnalisisSin(lineaSin, i + 1);
        }

        check.scope(scope);
        //printRWArray(arrayRW);
        setHex(arrayRW);
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
