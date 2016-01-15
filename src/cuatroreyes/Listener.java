/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuatroreyes;

import akka.actor.UntypedActor;

/**
 *
 * @author Ivan
 */
public class Listener extends UntypedActor{
    private String[][] tablero;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    
    public Listener(){
        //Inicializamos tablero con los valores del tablero inicial
        
        this.tablero = new String[8][8];
        for(int i=0;i<=7;i++){
            for(int j=0;j<=7;j++){
                tablero[i][j] = "  ";
            }
        }
        
        tablero[7][0] = "BN";//BARCO
        tablero[6][0] = "CN";//CABALLO
        tablero[5][0] = "EN";//ELEFANTE
        tablero[4][0] = "RN";//REY
        tablero[7][1] = "PN";//PEON
        tablero[6][1] = "PN";//PEON
        tablero[5][1] = "PN";//PEON
        tablero[4][1] = "PN";//PEON
        
        tablero[7][4] = "RV";
        tablero[7][5] = "EV";
        tablero[7][6] = "CV";
        tablero[7][7] = "BV";
        tablero[6][4] = "PV";
        tablero[6][5] = "PV";
        tablero[6][6] = "PV";
        tablero[6][7] = "PV";
        
        tablero[0][0] = "BB";
        tablero[0][1] = "CB";
        tablero[0][2] = "EB";
        tablero[0][3] = "RB";
        tablero[1][0] = "PB";
        tablero[1][1] = "PB";
        tablero[1][2] = "PB";
        tablero[1][3] = "PB";
        
        tablero[0][7] = "BR";
        tablero[1][7] = "CR";
        tablero[2][7] = "ER";
        tablero[3][7] = "RR";
        tablero[0][6] = "PR";
        tablero[1][6] = "PR";
        tablero[2][6] = "PR";
        tablero[3][6] = "PR";
        
        //Las siguientes entradas en el tablero son para testing, para no tirarme 4 turnos de cada color
        //para llegar a una situación que quiero. No deben aparecer en la versión final, obviamente
        tablero[1][2] = "  ";
        tablero[2][3] = "PB";
        tablero[4][0] = "  ";
        tablero[5][1] = "  ";
        tablero[4][2] = "PV";
        tablero[6][2] = "PB";
        tablero[6][6] = "PN";
        tablero[6][7] = "  ";
        tablero[1][5] = "PV";
        tablero[1][1] = "PR";
        tablero[1][0] = "  ";
        //Fin entradas de testing
    }
    
    @Override
    public void onReceive(Object msg) throws InterruptedException{
        if(msg instanceof Movimiento){ //Listener revisa que el movimiento sea correcto
            Movimiento m = (Movimiento) msg;
            
            try{
                if (movCorrecto(m)){
                    System.out.println(ANSI_GREEN+"Movimiento correcto"+ANSI_RESET); 
                }else{
                    System.out.println(ANSI_RED+"Movimiento incorrecto"+ANSI_RESET);
                    Thread.sleep(1000);//Para que de tiempo a leer el mensaje anterior
                    tableroActual();
                    VMovimiento vm = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                    getSender().tell(vm,getSelf());
                }
                //}
            }catch (NumberFormatException | StringIndexOutOfBoundsException ex){
                System.out.println(ANSI_RED+"Los valores del tablero que has introducido no son correctos"+ANSI_RESET);
                Thread.sleep(1000);//Para que de tiempo a leer el mensaje anterior
                tableroActual();
                VMovimiento vm = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                getSender().tell(vm,getSelf());
            }
        }else if(msg instanceof PlayGame){
            
            tableroActual();
            getSender().tell(new Mover(),getSelf());
        }
    }
    public void tableroActual(){
        System.out.println("  a  b  c  d  e  f  g  h  ");
        for(int fila = 7;fila>=0;fila--){
            System.out.println(" +--+--+--+--+--+--+--+--+");
            String filaStr = (fila+1) + "|";
            for(int columna=0;columna<=7;columna++){
                //Si añadimos un metodo para obtener la pieza de un array por 
                //Ejemplo de 8x8 y lo ponemos en " " entonces tambien mostramos la figura
                filaStr += getPiezaTablero(fila,columna) + "|";
            }
            System.out.println(filaStr + (fila + 1));
        }
        System.out.println(" +--+--+--+--+--+--+--+--+");
        System.out.println("  a  b  c  d  e  f  g  h  ");
    }
    
    public String getPiezaTablero(int fila,int columna){
        //Consultamos el contenido de la posición (fila,columna)
        return this.tablero[fila][columna];
    }
    
    public boolean movCorrecto(Movimiento m) throws NumberFormatException, StringIndexOutOfBoundsException{
        boolean correcto = false;
        
        int origfila = Integer.parseInt(m.getOrigen().substring(1));
        int destfila = Integer.parseInt(m.getDestino().substring(1));
        
        char charOrigencol = m.getOrigen().charAt(0);
        char charDestinocol = m.getDestino().charAt(0);
        
        int origcol = 9;
        int destcol = 9;

        if ((origfila > 8)  || (origfila < 1)){
            System.out.println(ANSI_RED+"Out of bounds del origen"+ANSI_RESET);
            return correcto;
        }
        if((destfila> 8)  || (destfila < 1)){                   
            System.out.println(ANSI_RED+"Out of bounds del destino"+ANSI_RESET);
            return correcto;
            /*VMovimiento vm = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
            getSender().tell(vm,getSelf());*/
        }

        else{
        //Movimiento correcto
            //Lo que se muestra por pantalla a continuación son los valores que entran, no los válidos para el array
            System.out.println("Origen"+charOrigencol+origfila+" Dest"+charDestinocol+destfila);
            destfila = destfila-1;//El subnromal de roberto no me deja ponerlo como -=
            origfila = origfila-1;
            
            
            switch(charDestinocol){
                case 'a':destcol = 0;
                    break;
                case 'b':destcol = 1;
                    break;
                case 'c':destcol = 2;
                    break;
                case 'd':destcol = 3;
                    break;
                case 'e':destcol = 4;
                    break;
                case 'f':destcol = 5;
                    break;
                case 'g':destcol = 6;
                    break;
                case 'h':destcol = 7;
                    break;
                default: return false; //Si no se puede hacer la conversión, el movimiento no es válido
            }
            switch(charOrigencol){
                case 'a':origcol = 0;
                    break;
                case 'b':origcol = 1;
                    break;
                case 'c':origcol = 2;
                    break;
                case 'd':origcol = 3;
                    break;
                case 'e':origcol = 4;
                    break;
                case 'f':origcol = 5;
                    break;
                case 'g':origcol = 6;
                    break;
                case 'h':origcol = 7;
                    break;
                default: return false;
            }
        }
        
        if((origcol == destcol) && (origfila == destfila)){
            return false;//No se ha movido la pieza
        }
        
        if (getPiezaTablero(destfila, destcol).charAt(1)!=m.getColor().charAt(0)){

            char color = getColorFromPos(origfila, origcol);
            char pieza = getPiezaFromPos(origfila, origcol);
            System.out.println(pieza+"   "+color);
            
            switch(pieza){
                case 'P':{
                    switch(color){
                        case 'B':{/////////////////////////////////////////////////////BLANCO
                            if(origcol == destcol){
                                if(origfila == (destfila-1)){
                                    if(destVacio(destfila, destcol)){
                                        //System.out.println("Mov peon blanco correcto");
                                        correcto = true;
                                    }
                                }else{

                                }
                            }else{
                                if(origcol == destcol+1 || origcol == destcol-1){
                                    if(!destVacio(destfila, destcol)){
                                        correcto = true;
                                    }
                                }
                            }
                            if(correcto){
                                if((destfila == 7) && (destcol == 6)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a CABALLO"+ANSI_RESET);
                                    tablero[origfila][origcol] = "CB";//Cambio el origen para que luego lo mueva, porque no puede cambiarse el destino aqui
                                }
                                if((destfila == 7) && (destcol == 5)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a ELEFANTE"+ANSI_RESET);
                                    tablero[origfila][origcol] = "EB";
                                }
                                if((destfila == 7) && (destcol == 2)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a ELEFANTE"+ANSI_RESET);
                                    tablero[origfila][origcol] = "EB";//Cambio el origen para que luego lo mueva, porque no puede cambiarse el destino aqui
                                }
                                if((destfila == 7) && (destcol == 1)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a CABALLO"+ANSI_RESET);
                                    tablero[origfila][origcol] = "CB";
                                }
                            }
                        break;
                        }
                        case 'N':{//////////////////////////////////////////////////////NEGRO
                            if(origfila == destfila){
                                if(origcol == (destcol-1)){
                                    if(destVacio(destfila, destcol)){
                                        //System.out.println("Mov peon Negro correcto");
                                        correcto = true;
                                    }
                                }else{
                                    //System.out.println("Else de columnas distintas");
                                }
                            }else{
                                if(origfila == destfila+1 || origfila == destfila-1){
                                    if(!destVacio(destfila, destcol)){
                                        //System.out.println("Peon negro ha comido");
                                        correcto = true;
                                    }
                                }
                            }
                            if(correcto){
                                if((destfila == 5) && (destcol == 7)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a ELEFANTE"+ANSI_RESET);
                                    tablero[origfila][origcol] = "EN";
                                }
                                if((destfila == 6) && (destcol == 7)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a CABALLO"+ANSI_RESET);
                                    tablero[origfila][origcol] = "CN";
                                }
                                if((destfila == 2) && (destcol == 7)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a ELEFANTE"+ANSI_RESET);
                                    tablero[origfila][origcol] = "EN";
                                }
                                if((destfila == 1) && (destcol == 7)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a CABALLO"+ANSI_RESET);
                                    tablero[origfila][origcol] = "CN";
                                }
                            }
                        break;}
                        case 'V':{///////////////////////////////////////////////////////////VERDE
                            if(origcol == destcol){
                                if(origfila == (destfila+1)){
                                    if(destVacio(destfila, destcol)){
                                        //System.out.println("Mov peon verde correcto");
                                        correcto = true;
                                    }
                                }else{

                                }
                            }else{
                                if(origcol == destcol+1 || origcol == destcol-1){
                                    if(!destVacio(destfila, destcol)){
                                        //System.out.println("Peon verde ha comido");
                                        correcto = true;
                                    }
                                }
                            }
                            if(correcto){
                                if((destfila == 0) && (destcol == 5)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a ELEFANTE"+ANSI_RESET);
                                    tablero[origfila][origcol] = "EV";
                                }
                                if((destfila == 0) && (destcol == 6)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a CABALLO"+ANSI_RESET);
                                    tablero[origfila][origcol] = "CV";
                                }
                                if((destfila == 0) && (destcol == 1)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a CABALLO"+ANSI_RESET);
                                    tablero[origfila][origcol] = "CV";
                                }
                                if((destfila == 0) && (destcol == 2)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a ELEFANTE"+ANSI_RESET);
                                    tablero[origfila][origcol] = "EV";
                                }
                            }
                        break;
                        }
                        case 'R':{//////////////////////////////////////////////////////ROJO
                            if(origfila == destfila){
                                if(origcol == (destcol+1)){
                                    if(destVacio(destfila, destcol)){
                                        //System.out.println("Mov peon rojo correcto");
                                        correcto = true;
                                    }
                                }else{

                                }
                            }else{
                                if(origfila == destfila+1 || origfila == destfila-1){
                                    if(!destVacio(destfila, destcol)){
                                        //System.out.println("Peon rojo ha comido");
                                        correcto = true;
                                    }
                                }
                            }
                            if(correcto){
                                if((destfila == 2) && (destcol == 0)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a ELEFANTE"+ANSI_RESET);
                                    tablero[origfila][origcol] = "ER";
                                }
                                if((destfila == 1) && (destcol == 0)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a CABALLO"+ANSI_RESET);
                                    tablero[origfila][origcol] = "CR";
                                }
                                if((destfila == 5) && (destcol == 0)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a ELEFANTE"+ANSI_RESET);
                                    tablero[origfila][origcol] = "ER";
                                }
                                if((destfila == 6) && (destcol == 0)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a CABALLO"+ANSI_RESET);
                                    tablero[origfila][origcol] = "CR";
                                }
                            }
                        break;
                        }
                    }
                }break;
                case 'R':{
                    correcto = movRey(origfila, origcol, destfila, destcol);
                    break;
                }
                case 'C':{
                    correcto = movCaballo(origfila, origcol, destfila, destcol);
                    break;
                }
                case 'E':{
                    correcto = movElefante(origfila, origcol, destfila, destcol);
                    break;
                }
                case 'B':{
                    correcto = movBarco(origfila, origcol, destfila, destcol);
                    break;
                }
                default:System.out.println("CHECK!!");
            }

            /*switch(color){
                case 'B':{/////////////////////////////////////BLANCO
                    switch(pieza){
                        case 'P':{//Peon
                            if(origcol == destcol){
                                if(origfila == (destfila-1)){
                                    if(destVacio(destfila, destcol)){
                                        //System.out.println("Mov peon blanco correcto");
                                        correcto = true;
                                    }
                                }else{

                                }
                            }else{
                                if(origcol == destcol+1 || origcol == destcol-1){
                                    if(!destVacio(destfila, destcol)){
                                        correcto = true;
                                    }
                                }
                            }
                            if(correcto){
                                if((destfila == 7) && (destcol == 2)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a ELEFANTE"+ANSI_RESET);
                                    tablero[origfila][origcol] = "EB";//Cambio el origen para que luego lo mueva, porque no puede cambiarse el destino aqui
                                }
                                if((destfila == 7) && (destcol == 1)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a CABALLO"+ANSI_RESET);
                                    tablero[origfila][origcol] = "CB";
                                }
                            }
                                
                        }break;
                        case 'R':{
                            correcto = movRey(origfila, origcol, destfila, destcol);
                            break;
                        }
                        case 'C':{
                            correcto = movCaballo(origfila, origcol, destfila, destcol);
                            break;
                        }
                        case 'E':{
                            correcto = movElefante(origfila, origcol, destfila, destcol);
                            break;
                        }
                        case 'B':{
                            correcto = movBarco(origfila, origcol, destfila, destcol);
                            break;
                        }
                    }
                }break;

                case 'N':{/////////////////////////////////////NEGRO
                    switch(pieza){
                        case 'P':{//Peon
                            if(origfila == destfila){
                                if(origcol == (destcol-1)){
                                    if(destVacio(destfila, destcol)){
                                        //System.out.println("Mov peon Negro correcto");
                                        correcto = true;
                                    }
                                }else{
                                    //System.out.println("Else de columnas distintas");
                                }
                            }else{
                                if(origfila == destfila+1 || origfila == destfila-1){
                                    if(!destVacio(destfila, destcol)){
                                        //System.out.println("Peon negro ha comido");
                                        correcto = true;
                                    }
                                }
                            }
                            if(correcto){
                                if((destfila == 5) && (destcol == 7)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a ELEFANTE"+ANSI_RESET);
                                    tablero[origfila][origcol] = "EN";
                                }
                                if((destfila == 6) && (destcol == 7)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a CABALLO"+ANSI_RESET);
                                    tablero[origfila][origcol] = "CN";
                                }
                            }
                        }break;
                        case 'R':{
                            correcto = movRey(origfila, origcol, destfila, destcol);
                            break;
                        }
                        case 'C':{
                            correcto = movCaballo(origfila, origcol, destfila, destcol);
                            break;
                        }
                        case 'E':{
                            correcto = movElefante(origfila, origcol, destfila, destcol);
                            break;
                        }
                        case 'B':{
                            correcto = movBarco(origfila, origcol, destfila, destcol);
                            break;
                        }
                    }
                }break;

                case 'V':{/////////////////////////////////////////VERDE
                    switch(pieza){
                        case 'P':{//Peon
                            if(origcol == destcol){
                                if(origfila == (destfila+1)){
                                    if(destVacio(destfila, destcol)){
                                        //System.out.println("Mov peon verde correcto");
                                        correcto = true;
                                    }
                                }else{

                                }
                            }else{
                                if(origcol == destcol+1 || origcol == destcol-1){
                                    if(!destVacio(destfila, destcol)){
                                        //System.out.println("Peon verde ha comido");
                                        correcto = true;
                                    }
                                }
                            }
                            if(correcto){
                                if((destfila == 0) && (destcol == 5)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a ELEFANTE"+ANSI_RESET);
                                    tablero[origfila][origcol] = "EV";
                                }
                                if((destfila == 0) && (destcol == 6)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a CABALLO"+ANSI_RESET);
                                    tablero[origfila][origcol] = "CV";
                                }
                            }
                        }break;
                        case 'R':{
                            correcto = movRey(origfila, origcol, destfila, destcol);
                            break;
                        }
                        case 'C':{
                            correcto = movCaballo(origfila, origcol, destfila, destcol);
                            break;
                        }
                        case 'E':{
                            correcto = movElefante(origfila, origcol, destfila, destcol);
                            break;
                        }
                        case 'B':{
                            correcto = movBarco(origfila, origcol, destfila, destcol);
                            break;
                        }
                    }
                }break;

                case 'R':{///////////////////////////////////////////ROJO
                    switch(pieza){
                        case 'P':{//Peon
                            if(origfila == destfila){
                                if(origcol == (destcol+1)){
                                    if(destVacio(destfila, destcol)){
                                        //System.out.println("Mov peon rojo correcto");
                                        correcto = true;
                                    }
                                }else{

                                }
                            }else{
                                if(origfila == destfila+1 || origfila == destfila-1){
                                    if(!destVacio(destfila, destcol)){
                                        //System.out.println("Peon rojo ha comido");
                                        correcto = true;
                                    }
                                }
                            }
                            if(correcto){
                                if((destfila == 2) && (destcol == 0)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a ELEFANTE"+ANSI_RESET);
                                    tablero[origfila][origcol] = "ER";
                                }
                                if((destfila == 1) && (destcol == 0)){
                                    System.out.println(ANSI_GREEN+"Peon promocionado a CABALLO"+ANSI_RESET);
                                    tablero[origfila][origcol] = "CR";
                                }
                            }
                        }break;
                        case 'R':{
                            correcto = movRey(origfila, origcol, destfila, destcol);
                            break;
                        }
                        case 'C':{
                            correcto = movCaballo(origfila, origcol, destfila, destcol);
                            break;
                        }
                        case 'E':{
                            correcto = movElefante(origfila, origcol, destfila, destcol);
                            break;
                        }
                        case 'B':{
                            correcto = movBarco(origfila, origcol, destfila, destcol);
                            break;
                        }
                    }
                }break;

                default:System.out.println("DEFAULT!!! CHECK");
            }*/
            if (correcto){
                tablero[destfila][destcol] = getPiezaTablero(origfila, origcol);
                tablero[origfila][origcol] = "  ";
                getSender().tell(new PlayGame(),getSelf());
            }
        }else{
            System.out.println(ANSI_RED+"Friendly fire"+ANSI_RESET);
        }

        return correcto;
    
    }
    
    public char getPiezaFromPos(int fila, int columna){
        return tablero[fila][columna].charAt(0);
    }
    
    public char getColorFromPos(int fila, int columna){
        return tablero[fila][columna].charAt(1);
    }
    
    public boolean destVacio(int destfila, int destcol){
        boolean vacio = false;
        if(getPiezaTablero(destfila, destcol).equals("  ")){
            vacio = true;
        }
        return vacio;
    }
    
    public boolean movRey(int origfila, int origcol, int destfila, int destcol){
        boolean reyOk = false;
        if(origfila == destfila){//Misma fila
            reyOk = (origcol == destcol+1 || origcol == destcol-1);
        }
        if(origcol == destcol){//Misma columna
            reyOk = (origfila == destfila+1 || origfila == destfila-1);
        }
        if(origfila != destfila && origcol != destcol){//Diagonal
            reyOk = ((origfila == destfila+1 || origfila == destfila-1) && (origcol == destcol+1 || origcol == destcol-1));
        }
        return reyOk;
    }
    
    public boolean movCaballo(int origfila, int origcol, int destfila, int destcol){
        boolean caballoOk = false;
        if((origcol == destcol+1) || (origcol == destcol-1)){
            caballoOk = (origfila == destfila+2) || (origfila == destfila-2);
        }
        if((origfila == destfila+1) || (origfila == destfila-1)){
            caballoOk = (origcol == destcol+2) || (origcol == destcol-2);
        }
        return caballoOk;
    }
    
    public boolean movElefante(int origfila, int origcol, int destfila, int destcol){
        boolean elefanteOk = true;
        int aux;
            if(origfila == destfila){
                if(origcol > destcol){//Va a la izquierda
                    for(aux = origcol-1; aux>destcol; aux--){
                        if(!destVacio(origfila,aux)){//Si en alguna hay una pieza, el movimiento es erróneo
                            return false;// Si se encuentra alguna pieza, no es necesario continuar con el bucle
                        }
                    }
                }
                if(origcol < destcol){//Va a la derecha
                    for(aux = origcol+1; aux<destcol; aux++){
                        if(!destVacio(origfila,aux)){//Si en alguna hay una pieza, el movimiento es erróneo
                            return false;
                        }
                    }
                }
            }
            if(origcol == destcol){
                if(origfila > destfila){//Va hacia abajo
                    for(aux = origfila-1; aux>destfila; aux--){
                        if(!destVacio(aux, origcol)){//Si en alguna hay una pieza, el movimiento es erróneo
                            return false;
                        }
                    }
                }
            }
            if(origcol == destcol){
                if(origfila < destfila){//Va hacia arriba
                    for(aux = origfila+1; aux<destfila; aux++){
                        if(!destVacio(aux, origcol)){//Si en alguna hay una pieza, el movimiento es erróneo
                            return false;
                        }
                    }
                }
            }
        return elefanteOk;
    }
    
    public boolean movBarco(int origfila, int origcol, int destfila, int destcol){
        boolean movBarco = false;
        if(origfila == destfila +2){
            movBarco = (origcol == destcol+2 || origcol == destcol-2);
        }
        if(origfila == destfila -2){
            movBarco = (origcol == destcol+2 || origcol == destcol-2);
        }
        return movBarco;
    }
    
    public boolean victoriaNaval(int destfila, int destcol){
        boolean victoria = false;
        
        return victoria;
    }
    
}
