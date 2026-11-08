package modelo;

import java.util.ArrayList;

import enums.StatusRobo;

public class Drone extends Robo {
    public boolean imagem;
    public int altitudeMax;
    
    public Drone(int id, String nome, StatusRobo status, boolean imagem, int altitudeMax){
        super.id = id;
        super.nome = nome;
        super.status = status;
        super.nivelBateria = 100;
        this.altitudeMax = altitudeMax;
        this.imagem = imagem;
        this.missoesRealizadas = new ArrayList<Missao>(0);
    }
    @Override 
    public boolean executarMissao(Missao missao){
        if(super.nivelBateria>=50){
            this.missoesRealizadas.add(missao);
            super.nivelBateria += -50;
        }
    }
    
}
