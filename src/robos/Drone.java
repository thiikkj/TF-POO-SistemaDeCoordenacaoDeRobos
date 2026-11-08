package robos;

import java.util.ArrayList;

import enums.StatusRobo;

public class Drone extends Robo {
    public boolean imagem;
    public int altitudeMax;
    public Drone(int id, String nome, StatusRobo status, boolean imagem, int altitudeMax){
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.nivelBateria = 100;
        this.altitudeMax = altitudeMax;
        this.imagem = imagem;
        this.missoesRealizadas = new ArrayList<Missao>(0);
    }
    public boolean executarMissao(Missao missao){
        if(this.nivelBateria>=50){
            this.missoesRealizadas.add(missao);
        }
    }
    
}
