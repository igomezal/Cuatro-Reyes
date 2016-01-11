/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuatroreyes;

import akka.actor.*;
import akka.routing.RoundRobinRouter;
import java.util.ArrayList;


/**
 *
 * @author Ivan
 */
public class Master extends UntypedActor{
    
    //RoundRobin
    ActorRef listener;
    ArrayList<ActorRef> jugadores;
    int numeroJugadores;
    ActorRef[] actor= new ActorRef[4];
    
    private final ActorRef workerRoute;

    int i;
            
    public Master(int numeroJugadores,ActorRef listener){
        //Creamos el actor Master con su listener y el numero de jugadores que jugaran
       
        
        this.listener = listener;
        this.numeroJugadores = numeroJugadores;
        jugadores = new ArrayList<>();
        actor[0] = this.getContext().actorOf(new Props(new UntypedActorFactory(){
                            @Override
                            public UntypedActor create(){
                                return new Player("Blanco");
                            }
                        }),"Blanco");
        actor[1] = this.getContext().actorOf(new Props(new UntypedActorFactory(){
                            @Override
                            public UntypedActor create(){
                                return new Player("Negro");
                            }
                        }),"Negro");
        actor[2] = this.getContext().actorOf(new Props(new UntypedActorFactory(){
                            @Override
                            public UntypedActor create(){
                                return new Player("Verde");
                            }
                        }),"Verde");
        actor[3] = this.getContext().actorOf(new Props(new UntypedActorFactory(){
                            @Override
                            public UntypedActor create(){
                                return new Player("Rojo");
                            }
                        }),"Rojo");
        i=0;
        while(i<numeroJugadores){
            jugadores.add(actor[i]);
            i++;
        }
        workerRoute = this.getContext().actorOf(new Props(Player.class).withRouter(RoundRobinRouter.create(jugadores)), "workerRouter");
    }

    
    @Override
    public void onReceive(Object msg){
        if(msg instanceof PlayGame){ //Pedimos al listener que nos muestre el tablero
            listener.tell(new PlayGame(),getSelf());
        }else if(msg instanceof Mover){ //Recorremos todo el enrutamiento
            workerRoute.tell(new Mover(),getSelf()); 
        }else if(msg instanceof Movimiento){ //La respuesta del jugador con su movimiento
            Movimiento m = (Movimiento) msg;
            listener.tell(m,getSelf());
        }else if(msg instanceof VMovimiento){ //Despueés de un movimiento incorrecto el jugador debe volver a mover
            VMovimiento vm = (VMovimiento) msg;
            vm.getActor().tell(new Mover(),getSelf());
        }else{
            unhandled(msg);
        }
    }
}
