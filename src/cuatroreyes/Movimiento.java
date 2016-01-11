/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuatroreyes;

import akka.actor.ActorRef;

/**
 *
 * @author Ivan
 */
public class Movimiento {
    //Clase para que podamos tener los datos sobre un movimiento
    private String color;
    private String origen;
    private String destino;
    private ActorRef actor;
    
    public Movimiento(ActorRef actor,String color,String origen,String destino){
        this.actor = actor;
        this.color = color;
        this.origen = origen;
        this.destino = destino;
    }

    public String getColor() {
        return color;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public ActorRef getActor() {
        return actor;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setActor(ActorRef actor) {
        this.actor = actor;
    }
    
}
