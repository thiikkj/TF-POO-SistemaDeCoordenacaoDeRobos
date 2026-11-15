package modelo;
//imports
import java.util.*;
import java.io.Serializable;
import enums.StatusRobo;
public abstract class Robo implements Comunicavel, Serializable{
    protected int id;
    public String nome;
    public int nivelBateria;
    public StatusRobo status;
    public ArrayList<Missao> missoesRealizadas;
    public Robo(int id, String nome, StatusRobo status){
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.nivelBateria = 100;
        this.missoesRealizadas = new ArrayList<Missao>(0);
    }
    //getters e setters
    public int getId(){
        return this.id;
    }
    public String getNome(){
        return this.nome;
    }
    public StatusRobo getStatus(){
        return this.status;
    }
    public int getNivelBateria(){
        return this.nivelBateria;
    }
    public void setNivelBateria(int bateria){
        this.nivelBateria = bateria;
    }
    public void setStatus(StatusRobo status){
        this.status = status;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setId(int id){
        this.id = id;
    }
    //-------------------------------\\
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
    public abstract boolean verificarCompatibilidadeArea(AreaOperacao area);
    public abstract boolean executarMissao(Missao missao);
    public abstract String exibirResumo();
    public void enviarMensagem(String mensagem){
        System.out.print("Mensagem enviada: "+mensagem);
    }
    public void receberMensagem(String mensagem){
        System.out.print("Mensagem recebida: "+mensagem);
    }

    
}
