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
        
        tablero[7][0] = "BN";
        tablero[6][0] = "CN";
        tablero[5][0] = "EN";
        tablero[4][0] = "RN";
        tablero[7][1] = "PN";
        tablero[6][1] = "PN";
        tablero[5][1] = "PN";
        tablero[4][1] = "PN";
        
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
                int origenfila = Integer.parseInt(m.getOrigen().substring(1));
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
                }*/else{
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
                            break;
                    }
                    tablero[destinofila-1][destcol] = getPiezaTablero(origenfila-1, origcol);
                    tablero[origenfila-1][origcol] = "  ";
                    getSender().tell(new PlayGame(),getSelf());
                }
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
    
}
