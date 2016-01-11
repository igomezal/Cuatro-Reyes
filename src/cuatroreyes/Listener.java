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
    
    @Override
    public void onReceive(Object msg){
        if(msg instanceof Movimiento){ //Listener revisa que el movimiento sea correcto
            Movimiento m = (Movimiento) msg;
            if("Negro".equals(m.getColor())){
                VMovimiento vm = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                getSender().tell(vm,getSelf());
            }else{
                getSender().tell(new PlayGame(),getSelf());
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
                filaStr += "  " + "|";
            }
            System.out.println(filaStr + (fila + 1));
        }
        System.out.println(" +--+--+--+--+--+--+--+--+");
        System.out.println("  a  b  c  d  e  f  g  h  ");
    }
    
}
