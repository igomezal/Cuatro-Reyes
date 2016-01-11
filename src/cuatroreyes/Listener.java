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
            if(m.getColor()=="Negro"){
                VMovimiento vm = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                getSender().tell(vm,getSelf());
            }else{
                getSender().tell(new Mover(),getSelf());
            }
        }
    }
    
}
