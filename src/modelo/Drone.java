package modelo;

import java.util.ArrayList;
    
import enums.StatusRobo;

public class Drone extends Robo {
    public boolean imagem;
    public int altitudeMax;
    //getters e setters
    public boolean getImagem(){
        return this.imagem;
    }
    public int getAltitudeMax(){
        return this.altitudeMax;
    }
    public void setImagem(boolean imagem){
        this.imagem = imagem;
    }
    public void setAltitudeMax(int altitudeMax){
        this.altitudeMax = altitudeMax;
    }
    //-------------------------------\\
    public Drone(int id, String nome, StatusRobo status, boolean imagem, int altitudeMax){
        super(id, nome, status);
        super.nivelBateria = 100;
        this.altitudeMax = altitudeMax;
        this.imagem = imagem;
        this.missoesRealizadas = new ArrayList<Missao>(0);
    }
    public boolean executarMissao(Missao missao){
        if(super.nivelBateria>=50){
            this.missoesRealizadas.add(missao);
            super.nivelBateria -= 50;
            return true;
        }
        return false;
    }
    
}
