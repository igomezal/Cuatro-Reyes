/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuatroreyes;


import akka.actor.UntypedActor;
import java.util.Scanner;

/**
 *
 * @author Ivan
 */
public class Player extends UntypedActor{
    protected String color;
    public Player(String color){
        this.color = color;
    }
    @Override
    public void onReceive(Object msg){
        if(msg instanceof Mover){ //El jugador mueve
            Scanner s = new Scanner(System.in);
            String origen;
            System.out.println("Introduce origen "+color);
            origen = s.nextLine();
            String destino;
            System.out.println("Introduce destino "+color);
            destino = s.nextLine();
            getSender().tell(new Movimiento(getSelf(),color,origen,destino),getSelf());
        }else{
            unhandled(msg);
        }
    }
    
}
