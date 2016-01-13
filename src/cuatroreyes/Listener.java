/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuatroreyes;

import akka.actor.UntypedActor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Ivan
 */
public class Listener extends UntypedActor{
    private String[][] tablero;
    
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
    }
    
    @Override
    public void onReceive(Object msg) throws InterruptedException{
        if(msg instanceof Movimiento){ //Listener revisa que el movimiento sea correcto
            Movimiento m = (Movimiento) msg;
            
            try{
                /*int origenfila = Integer.parseInt(m.getOrigen().substring(1));
                int destinofila = Integer.parseInt(m.getDestino().substring(1));
                char origencol = m.getOrigen().charAt(0);
                char destinocol = m.getDestino().charAt(0);
               
                if ((origenfila > 8)  || (origenfila < 1)){
                    System.out.println("Out of bounds del origen");
                    VMovimiento vm = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                    getSender().tell(vm,getSelf());
                }
                if((destinofila> 8)  || (destinofila < 1)){                   
                    System.out.println("Out of bounds del destino");
                    VMovimiento vm = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                    getSender().tell(vm,getSelf());
                }


                /*if("Negro".equals(m.getColor())){
                    VMovimiento vm = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                    getSender().tell(vm,getSelf());
                }*else{
                    //Movimiento correcto
                    int origcol = 4;
                    int destcol = 4;
                    switch(destinocol){
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
                    }
                    switch(origencol){
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
                            break;*/
                    //}
            if (movCorrecto(m)){
                        /*tablero[destinofila-1][destcol] = getPiezaTablero(origenfila-1, origcol);
                        tablero[origenfila-1][origcol] = "  ";
                        getSender().tell(new PlayGame(),getSelf());*/
                System.out.println("Movimiento correcto"); 
            }else{
                System.out.println("Movimiento incorrecto");
                Thread.sleep(1000);//Para que de tiempo a leer el mensaje anterior
                tableroActual();
                VMovimiento vm = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                getSender().tell(vm,getSelf());
            }
                //}
            }catch (NumberFormatException ex){
                System.out.println("Los valores del tablero que has introducido no son correctos");
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
    
    public boolean movCorrecto(Movimiento m) throws NumberFormatException{
        boolean correcto = false;
        
        int origfila = Integer.parseInt(m.getOrigen().substring(1));
        int destfila = Integer.parseInt(m.getDestino().substring(1));
        
        char charOrigencol = m.getOrigen().charAt(0);
        char charDestinocol = m.getDestino().charAt(0);
        
        int origcol = 9;
        int destcol = 9;

        if ((origfila > 8)  || (origfila < 1)){
            System.out.println("Out of bounds del origen");
            return correcto;
        }
        if((destfila> 8)  || (destfila < 1)){                   
            System.out.println("Out of bounds del destino");
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
            }
        }
        
        char color = getColorFromPos(origfila, origcol);
        char pieza = getPiezaFromPos(origfila, origcol);
        System.out.println(pieza+"   "+color);
        
        switch(color){
            case 'B':{/////////////////////////////////////BLANCO
                switch(pieza){
                    case 'P':{//Peon
                        //La columna tiene que ser la misma, de momento no se comprueba el comer en diagonal
                        if(origcol == destcol)//Añadir else aqui para ver si se come
                            if(origfila == (destfila-1)){
                                System.out.println("Mov peon blanco correcto");
                                correcto = true;
                            }
                    }break;
                }
            }break;
            
            case 'N':{/////////////////////////////////////NEGRO
                switch(pieza){
                    case 'P':{//Peon
                        //La fila tiene que ser la misma, de momento no se comprueba el comer en diagonal
                        System.out.println("Debug Peon NEgro");
                        if(origfila == destfila)
                            if(origcol == (destcol-1)){
                                System.out.println("Mov peon negro correcto");
                                correcto = true;
                            }else
                                System.out.println("Else de columnas distintas");
                        else
                            System.out.println("Else de filas distintas");
                    }break;
                }
            }break;
            
            case 'V':{/////////////////////////////////////////VERDE
                switch(pieza){
                    case 'P':{//Peon
                        //La columna tiene que ser la misma, de momento no se comprueba el comer en diagonal
                        if(origcol == destcol)//Añadir else aqui para ver si se come
                            if(origfila == (destfila+1)){
                                System.out.println("Mov peon verde correcto");
                                correcto = true;
                            }
                    }break;
                }
            }break;
                
            case 'R':{///////////////////////////////////////////ROJO
                switch(pieza){
                    case 'P':{//Peon
                        //La columna fila que ser la misma, de momento no se comprueba el comer en diagonal
                        if(origfila == destfila)
                            if(origcol == (destcol+1)){
                                System.out.println("Mov peon Rojo correcto");
                                correcto = true;
                            }
                    }break;
                }
            }break;
                
            default:System.out.println("DEFAULT!!! CHECK");
        }
        if (correcto){
            tablero[destfila][destcol] = getPiezaTablero(origfila, origcol);
            tablero[origfila][origcol] = "  ";
            getSender().tell(new PlayGame(),getSelf());
        }

        return correcto;
    
    }
    
    public char getPiezaFromPos(int fila, int columna){
        return tablero[fila][columna].charAt(0);
    }
    
    public char getColorFromPos(int fila, int columna){
        return tablero[fila][columna].charAt(1);
    }
    
}
