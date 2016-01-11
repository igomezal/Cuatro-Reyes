/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuatroreyes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 *
 * @author Ivan
 */
public class Cuatroreyes {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("Game");
        final ActorRef listener = system.actorOf(new Props(Listener.class), "listener");
        
        ActorRef master = system.actorOf(new Props(() -> {
            return new Master(4,listener);
        }), "master");
        
        master.tell(new Mover());
    }
    
}
