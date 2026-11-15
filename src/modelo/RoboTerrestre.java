package modelo;

import java.util.*;
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
        int prioridade;
        if(missao.getPrioridade() ==enums.PrioridadeMissao.ALTA){
            prioridade = 10;
        }
        else if(missao.getPrioridade() == enums.PrioridadeMissao.MEDIA){
            prioridade = 5;
        }
        else{
            prioridade = 0;
        }
        if(missao.getTipo() == enums.TipoMissao.LOCALIZACAO_PESSOAS && super.nivelBateria>=40 + prioridade){
            super.missoesRealizadas.add(missao);
            super.nivelBateria -= 40 + prioridade;
            return true;
        }
        else if(missao.getTipo() == enums.TipoMissao.TRANSPORTE_SUPRIMENTOS && super.nivelBateria>=50 + prioridade){
            super.missoesRealizadas.add(missao);
            super.nivelBateria -= 50 + prioridade;
            return true;
        }  
        return false;
    }
    @Override 
    public String exibirResumo(){
        return "Id: "+this.id+"\n"+"Nome: "+this.nome+"\n"+"Bateria: "+this.nivelBateria+"\n"+"Status: "+this.status+"\n"+"Quantidade de Missões: "+this.missoesRealizadas.size()+"\n"+"Velocidade Máxima: "+this.velocidadeMax+"\n"+"Terreno: "+this.terreno+"\n"+"Carga Máxima: "+this.cargaMax;
    }
    @Override 
    public boolean verificarCompatibilidadeArea(AreaOperacao area){
        if(area.getTipoTerreno() == this.terreno){
            return true;
        }
        return false;
    }
    @Override 
    public boolean checarTipoMissao(Missao missao){
        if(missao.getTipo() == enums.TipoMissao.LOCALIZACAO_PESSOAS || missao.getTipo() == enums.TipoMissao.TRANSPORTE_SUPRIMENTOS){
            return true;
        }
        return false;
    }
}