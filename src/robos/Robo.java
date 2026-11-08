package robos;
//imports
import java.util.ArrayList;

import enums.StatusRobo;
public abstract class Robo {
    private int id;
    public String nome;
    public int nivelBateria;
    public StatusRobo status;
    public ArrayList<Missoes> missoesRealizadas;
    public int getId(){
        return this.id;
    }
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
    abstract public void executarMissao(Missao missao);
    
}
