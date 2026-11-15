package modelo;

import java.util.ArrayList;
import enums.StatusRobo;
import enums.TipoTerreno;

public class RoboTerrestre extends Robo {
    public int velocidadeMax;
    public TipoTerreno terreno;
    public double cargaMax;
    //getters e setters
    public int getVelocidadeMax(){
        return this.velocidadeMax;
    }
    public TipoTerreno getTerreno(){
        return this.terreno;
    }
    public void setVelocidadeMax(int velocidadeMax){
        this.velocidadeMax = velocidadeMax;
    }
    public double getCargaMax(){
        return this.cargaMax;
    }
    public void setCargaMax(double cargaMax){
        this.cargaMax = cargaMax;
    }
    //-------------------------------\\

    public RoboTerrestre(int id, String nome, StatusRobo status, int velocidadeMax, TipoTerreno terreno, double cargaMax){
        super(id, nome, status);
        this.velocidadeMax = velocidadeMax;
        this.terreno = terreno;
        this.cargaMax = cargaMax;
        super.missoesRealizadas = new ArrayList<Missao>(0);
    }

    @Override 
    public boolean executarMissao(Missao missao){
        if(super.nivelBateria>=50){
            super.missoesRealizadas.add(missao);
            super.nivelBateria += -50;
            return true;
        }
        return false;
    }
}