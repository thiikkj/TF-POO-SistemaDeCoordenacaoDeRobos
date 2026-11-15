package modelo;

import java.util.*;
    
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
        if(missao.getTipo() == enums.TipoMissao.RECONHECIMENTO && super.nivelBateria>=30){
            super.missoesRealizadas.add(missao);
            super.nivelBateria -= 30 + prioridade;
            return true;
        }
        else if(missao.getTipo() == enums.TipoMissao.LOCALIZACAO_PESSOAS && super.nivelBateria>=40){
            super.missoesRealizadas.add(missao);
            super.nivelBateria -= 40 + prioridade;
            return true;
        }
        else if(missao.getTipo() == enums.TipoMissao.TRANSPORTE_SUPRIMENTOS && super.nivelBateria>=50){
            super.missoesRealizadas.add(missao);
            super.nivelBateria -= 50 + prioridade;
            return true;
        }
        return false;
    }
    @Override
    public boolean verificarCompatibilidadeArea(AreaOperacao area){
        return true;
    }
    @Override
    public String exibirResumo(){
        return "Id: "+this.id+"\n"+"Nome: "+this.nome+"\n"+"Bateria: "+this.nivelBateria+"\n"+"Status: "+this.status+"\n"+"Quantidade de Missões: "+this.missoesRealizadas.size()+"\n"+"Imagem: "+this.imagem+"\n"+"Altitude Máxima: "+this.altitudeMax;
    }
    
}
