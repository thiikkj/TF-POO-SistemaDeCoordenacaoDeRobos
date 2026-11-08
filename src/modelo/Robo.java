package modelo;
//imports
import java.util.ArrayList;

import enums.StatusRobo;
public abstract class Robo implements Comunicavel{
    protected int id;
    public String nome;
    public int nivelBateria;
    public StatusRobo status;
    public ArrayList<Missao> missoesRealizadas;
    
    public void setNivelBateria(int bateria){
        this.nivelBateria = bateria;
    }
    protected StatusRobo verificarDisponibilidade(){
        return this.status;
    }
    protected int consultarBateria(){
        return this.nivelBateria;
    }
    protected String recarregar(){
        setNivelBateria(100);
        return "Bateria Recarregada";
    }
    protected String exibirResumo(){
        return "Id: "+this.id+"\n"+"Nome: "+this.nome+"\n"+"Bateria: "+this.nivelBateria+"\n"+"Status: "+this.status+"\n"+"Quantidade de Missões: "+this.missoesRealizadas.size();
    }
    public boolean executarMissao(Missao missao){
        if(this.nivelBateria>=50){
            this.missoesRealizadas.add(missao);
        }
    }
    public void enviarMensagem(String mensagem){
        System.out.print("Mensagem enviada: "+mensagem);
    }
    public void receberMensagem(String mensagem){
        System.out.print("Mensagem recebida: "+mensagem);
    }
    
}
