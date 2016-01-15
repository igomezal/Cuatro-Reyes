/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuatroreyes;

import akka.actor.UntypedActor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Ivan
 */
public class Listener extends UntypedActor{
    private String[][] tablero;
    private int[] puntuacion;
    private int[] reyes;
    private int[] reyesComidos;
    private ArrayList<Character> tronosB;
    private ArrayList<Character> tronosN;
    private ArrayList<Character> tronosV;
    private ArrayList<Character> tronosR;
    
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
        
        puntuacion = new int[4];
        reyes = new int[4];
        for (int i=0;i<4;i++){
            reyes[i]=1;
        }
        reyesComidos = new int[4];
        tronosB = new ArrayList<Character>();
        tronosN = new ArrayList<Character>();
        tronosV = new ArrayList<Character>();
        tronosR = new ArrayList<Character>();
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
                boolean acabado = false;
               
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
                    if (getPiezaTablero(destinofila-1, destcol)!= "  " && getPiezaTablero(destinofila-1, destcol).charAt(1)!=m.getColor().charAt(0)){
                        switch(getPiezaTablero(destinofila-1, destcol).charAt(0)){
                            case 'P': System.out.println("Has comido un peón, ganas un punto");
                                comer(1,m.getColor().charAt(0));
                                break;
                            case 'B': System.out.println("Has comido un barco, ganas dos puntos");
                                comer(2,m.getColor().charAt(0));
                                break;
                            case 'C': System.out.println("Has comido un caballo, ganas tres puntos");
                                comer(3,m.getColor().charAt(0));
                                break;
                            case 'E': System.out.println("Has comido un elefante, ganas cuatro puntos");
                                comer(4,m.getColor().charAt(0));
                                break;
                            case 'R': System.out.println("Has comido un rey, ganas cinco puntos");
                                comer(5,m.getColor().charAt(0));
                                acabado = comerRey(m.getColor().charAt(0),getPiezaTablero(destinofila-1, destcol).charAt(1));
                                break;
                        }
                    }
                   
                    
                    if ((getPiezaTablero(origenfila-1, origcol).charAt(0)=='R')&&(getPiezaTablero(destinofila-1, destcol).charAt(1)!=m.getColor().charAt(0))){
                        if(((destinofila-1==4)&&(destcol==0))||((destinofila-1==7)&&(destcol==4))||((destinofila-1==0)&&(destcol==3))||((destinofila-1==3)&&(destcol==7))){
                        if ((destinofila-1==4)&&(destcol==0)){
                            if ((getPiezaTablero(origenfila-1,origcol).charAt(1))!= 'N'){
                            switch(m.getColor().charAt(0)){
                                case 'B':
                                    if (!tronosB.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosB.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                    if (tronosB.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[0]=puntuacion[0]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm1 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm1.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm1,getSelf());
                                    
                                    break;
                                case 'N':
                                    if (!tronosN.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosN.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                     if (tronosN.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[1]=puntuacion[1]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm2 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm2.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm2,getSelf());
                                    
                                    break;
                                case 'V': 
                                    if (!tronosV.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosV.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                     if (tronosV.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[2]=puntuacion[2]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm3 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm3.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm3,getSelf());
                                    
                                    break;
                                case 'R':
                                    if (!tronosR.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosR.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                     if (tronosR.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[3]=puntuacion[3]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm4 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm4.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm4,getSelf());
                                    
                                    break; 
                            }
                        } else{
                                getSender().tell(new PlayGame(),getSelf());
                            }
                        }
                        
                        if ((destinofila-1==7)&&(destcol==4)){
                            if ((getPiezaTablero(origenfila-1,origcol).charAt(1))!= 'V'){
                             switch(m.getColor().charAt(0)){
                                case 'B':
                                    if (!tronosB.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosB.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                    if (tronosB.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[0]=puntuacion[0]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm1 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm1.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm1,getSelf());
                                    
                                    break;
                                case 'N':
                                    if (!tronosN.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosN.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                     if (tronosN.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[1]=puntuacion[1]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm2 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm2.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm2,getSelf());
                                    
                                    break;
                                case 'V': 
                                    if (!tronosV.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosV.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                     if (tronosV.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[2]=puntuacion[2]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm3 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm3.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm3,getSelf());
                                    
                                    break;
                                case 'R':
                                    if (!tronosR.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosR.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                     if (tronosR.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[3]=puntuacion[3]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm4 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm4.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm4,getSelf());
                                    
                                    break; 
                            }
                            } else{
                                getSender().tell(new PlayGame(),getSelf());
                            }
                        }
                           
                        if ((destinofila-1==0)&&(destcol==3)){
                            if ((getPiezaTablero(origenfila-1,origcol).charAt(1))!= 'B'){
                             switch(m.getColor().charAt(0)){
                                case 'B':
                                    if (!tronosB.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosB.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                    if (tronosB.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[0]=puntuacion[0]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm1 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm1.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm1,getSelf());
                                    
                                    break;
                                case 'N':
                                    if (!tronosN.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosN.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                     if (tronosN.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[1]=puntuacion[1]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm2 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm2.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm2,getSelf());
                                    
                                    break;
                                case 'V': 
                                    if (!tronosV.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosV.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                     if (tronosV.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[2]=puntuacion[2]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm3 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm3.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm3,getSelf());
                                    
                                    break;
                                case 'R':
                                    if (!tronosR.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosR.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                     if (tronosR.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[3]=puntuacion[3]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm4 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm4.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm4,getSelf());
                                    
                                    break; 
                            }
                            } else{
                                getSender().tell(new PlayGame(),getSelf());
                            }
                        }
                        
                        if ((destinofila-1==3)&&(destcol==7)){
                            if ((getPiezaTablero(origenfila-1,origcol).charAt(1))!= 'R'){
                              switch(m.getColor().charAt(0)){
                                case 'B':
                                    if (!tronosB.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosB.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                    if (tronosB.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[0]=puntuacion[0]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm1 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm1.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm1,getSelf());
                                    
                                    break;
                                case 'N':
                                    if (!tronosN.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosN.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                     if (tronosN.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[1]=puntuacion[1]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm2 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm2.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm2,getSelf());
                                    
                                    break;
                                case 'V': 
                                    if (!tronosV.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosV.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                     if (tronosV.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[2]=puntuacion[2]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm3 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm3.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    getSender().tell(vm3,getSelf());
                                    
                                    break;
                                case 'R':
                                    if (!tronosR.contains(getPiezaTablero(destinofila-1, destcol).charAt(1))){
                                    tronosR.add(getPiezaTablero(destinofila-1, destcol).charAt(1));
                                    }
                                     if (tronosR.size()==3){
                                        for (int i=0;i<8;i++){
                                            for(int j=0;j<8;j++){
                                                if(this.tablero[i][j]=="EB"){
                                                    System.out.println("Enhorabuena has conseguido capturar todos los tronos");
                                                    puntuacion[3]=puntuacion[3]*4;
                                                    finalJuego();
                                                }
                                            }
                                        } 
                                    }
                                    VMovimiento vm4 = new VMovimiento(m.getActor(),m.getColor(),m.getOrigen(),m.getDestino());
                                    vm4.setVuelveAjugar(true);
                                    mostrarPuntuaciones();
                                    
                                    getSender().tell(vm4,getSelf());
                                    
                                    break; 
                            }
                            } else{
                                getSender().tell(new PlayGame(),getSelf());
                            }
                        }
                            tablero[destinofila-1][destcol] = getPiezaTablero(origenfila-1, origcol);
                            tablero[origenfila-1][origcol] = "  ";
                            tableroActual();  
                        }else{
                            tablero[destinofila-1][destcol] = getPiezaTablero(origenfila-1, origcol);
                            tablero[origenfila-1][origcol] = "  ";
                            tableroActual();  
                            getSender().tell(new PlayGame(),getSelf());
                        } 
                    } 
                    else{
                    mostrarPuntuaciones();
                    
                    
                    tablero[destinofila-1][destcol] = getPiezaTablero(origenfila-1, origcol);
                    tablero[origenfila-1][origcol] = "  ";
                        if (acabado == true){
                            System.out.println("El juego ha acabado:");
                            int j=0;
                            for (int i=0; i<4; i++){
                                if (puntuacion[i]>j){
                                    j=puntuacion[i];
                                }
                            }
                            finalJuego();
                        } 
                    
                    
                    getSender().tell(new PlayGame(),getSelf());  
                    }  
                    
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
    
    public void finalJuego(){  
        int j=0;
        String color =" ";
        int z=0;
        for (int i=0; i<4; i++){
            if(puntuacion[i]>j){
               j=puntuacion[i]; 
               z=i;
            }
            if (z==0){color = "Blanco";}
            if (z==1){color = "Negro";}
            if (z==2){color = "Verde";}
            if (z==3){color = "Rojo";}
        }
        System.out.println("Enhorabuena jugador "+color+", has ganado con una puntuación de "+j);
        getSender().tell(new FinishGame(), getSelf());
        getContext().stop(getSelf());
        getContext().system().shutdown();
    }
    
    public void comer(int i,char c){
        if (i==5){
            switch(c){
                case 'B': this.reyesComidos[0]+=1;
                    break;
                case 'N': this.reyesComidos[1]+=1;
                    break;
                case 'V': this.reyesComidos[2]=+1;
                    break;
                case 'R': this.reyesComidos[3]=+1;
                    break;
            }
        }
        switch(c){
            case 'B': this.puntuacion[0]+=i;
                break;
            case 'N': this.puntuacion[1]+=i;
                break;
            case 'V': this.puntuacion[2]=+i;
                break;
            case 'R': this.puntuacion[3]=+i;
                break;
        }
        
    }
    public boolean comerRey(char c,char r){
        
        switch(r){
            case 'B': this.reyes[0]=0;
                switch(c){
                    case 'B': if(reyesComidos[0]==3 && reyes[0]==1){
                        this.puntuacion[0]+=54;
                        return true;
                    }
                    break;
                    case 'N': if(reyesComidos[1]==3 && reyes[1]==1){ 
                        this.puntuacion[1]+=54;
                        return true;
                    }break;
                    case 'V': if(reyesComidos[2]==3 && reyes[2]==1){
                        this.puntuacion[2]=+54;
                        return true;
                    }break;
                    case 'R':  if(reyesComidos[3]==3 && reyes[3]==1){
                        this.puntuacion[3]=+54;
                        return true;
                    }break;
                }
            break;
            case 'N': this.reyes[1]=0;
                switch(c){
                    case 'B': if(reyesComidos[0]==3 && reyes[0]==1){
                        this.puntuacion[0]+=54;
                        return true;
                    }
                    break;
                    case 'N': if(reyesComidos[1]==3 && reyes[1]==1){ 
                        this.puntuacion[1]+=54;
                        return true;
                    }break;
                    case 'V': if(reyesComidos[2]==3 && reyes[2]==1){
                        this.puntuacion[2]=+54;
                        return true;
                    }break;
                    case 'R':  if(reyesComidos[3]==3 && reyes[3]==1){
                        this.puntuacion[3]=+54;
                        return true;
                    }break;
                }
            break;
            case 'V': this.reyes[2]=0;
                switch(c){
                    case 'B': if(reyesComidos[0]==3 && reyes[0]==1){
                        this.puntuacion[0]+=54;
                        return true;
                    }
                    break;
                    case 'N': if(reyesComidos[1]==3 && reyes[1]==1){ 
                        this.puntuacion[1]+=54;
                        return true;
                    }break;
                    case 'V': if(reyesComidos[2]==3 && reyes[2]==1){
                        this.puntuacion[2]=+54;
                        return true;
                    }break;
                    case 'R':  if(reyesComidos[3]==3 && reyes[3]==1){
                        this.puntuacion[3]=+54;
                        return true;
                    }break;
                }
            break;
            case 'R': this.reyes[3]=0;
                switch(c){
                    case 'B': if(reyesComidos[0]==3 && reyes[0]==1){
                        this.puntuacion[0]+=54;
                        return true;
                    }
                    break;
                    case 'N': if(reyesComidos[1]==3 && reyes[1]==1){ 
                        this.puntuacion[1]+=54;
                        return true;
                    }break;
                    case 'V': if(reyesComidos[2]==3 && reyes[2]==1){
                        this.puntuacion[2]=+54;
                        return true;
                    }break;
                    case 'R':  if(reyesComidos[3]==3 && reyes[3]==1){
                        this.puntuacion[3]=+54;
                        return true;
                    }break;
                }
            break;
        }
        return false;
    }
    
    public void mostrarPuntuaciones(){
        System.out.println("Jugador Blanco: "+this.puntuacion[0]+" puntos.");
        System.out.println("Jugador Negro: "+this.puntuacion[1]+" puntos.");
        System.out.println("Jugador Verde: "+this.puntuacion[2]+" puntos.");
        System.out.println("Jugador Rojo: "+this.puntuacion[3]+" puntos.");
    }
    
    public String getPiezaTablero(int fila,int columna){
        //Consultamos el contenido de la posición (fila,columna)
        return this.tablero[fila][columna];
    }
    
}
