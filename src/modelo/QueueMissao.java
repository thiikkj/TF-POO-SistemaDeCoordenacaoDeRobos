package modelo;
import java.util.*;

public class QueueMissao {
    public Queue<Missao> filaMissao;
    public Queue<Missao> prioridadeAlta;
    public Queue<Missao> prioridadebaixa;
    //-------------------------------\\
    public Queue<Missao> getFilaMissao(){
        return this.filaMissao;
    }
    public Queue<Missao> getPrioridadeAlta(){
        return this.prioridadeAlta;
    }
    public Queue<Missao> getPrioridadeBaixa(){
        return this.prioridadebaixa;
    }
    public void setFilaMissao(Queue<Missao> filaMissao){
        this.filaMissao = filaMissao;
    }
    public void setPrioridadeAlta(Queue<Missao> prioridadeAlta){
        this.prioridadeAlta = prioridadeAlta;
    }
    public void setPrioridadeBaixa(Queue<Missao> prioridadebaixa){
        this.prioridadebaixa = prioridadebaixa;
    }
    //-------------------------------\\
    public QueueMissao(){
        this.filaMissao = new LinkedList<Missao>();
        this.prioridadeAlta = new LinkedList<Missao>();
        this.prioridadebaixa = new LinkedList<Missao>();
    }
    //-------------------------------\\
    public void adicionarMissao(Missao missao){
        if(missao.getPrioridade() == enums.PrioridadeMissao.ALTA){
            this.prioridadeAlta.add(missao);
        }
        else if(missao.getPrioridade() == enums.PrioridadeMissao.MEDIA){
            this.filaMissao.add(missao);
        }
        else{
            this.prioridadebaixa.add(missao);
        }
    }
    public Missao removerMissao(){
        if(!this.prioridadeAlta.isEmpty()){
            return this.prioridadeAlta.poll();
        }
        else if(!this.filaMissao.isEmpty()){
            return this.filaMissao.poll();
        }
        else if(!this.prioridadebaixa.isEmpty()){
            return this.prioridadebaixa.poll();
        }
        else{
            return null;
        }
    }
    public boolean isEmpty(){
        return this.prioridadeAlta.isEmpty() && this.filaMissao.isEmpty() && this.prioridadebaixa.isEmpty();
    }
    public int size(){
        return this.prioridadeAlta.size() + this.filaMissao.size() + this.prioridadebaixa.size();
    }
    public void limparFila(){
        this.prioridadeAlta.clear();
        this.filaMissao.clear();
        this.prioridadebaixa.clear();
    }
    public void imprimirFila(){
        System.out.println("Fila de Missões:");
        System.out.println("Prioridade Alta:");
        for(Missao missao : this.prioridadeAlta){
            System.out.println(missao);
        }
        System.out.println("Prioridade Média:");
        for(Missao missao : this.filaMissao){
            System.out.println(missao);
        }
        System.out.println("Prioridade Baixa:");
        for(Missao missao : this.prioridadebaixa){
            System.out.println(missao);
        }
    }
    public void imprimirFilaResumo(){
        System.out.println("Fila de Missões:");
        System.out.println("Prioridade Alta: " + this.prioridadeAlta.size());
        System.out.println("Prioridade Média: " + this.filaMissao.size());
        System.out.println("Prioridade Baixa: " + this.prioridadebaixa.size());
    }
    public String toString(){
        return "Fila de Missões: " + this.size() + "\nPrioridade Alta: " + this.prioridadeAlta.size() + "\nPrioridade Média: " + this.filaMissao.size() + "\nPrioridade Baixa: " + this.prioridadebaixa.size();
    }
    public void realizarMissao(){
        Missao missao = this.removerMissao();
        if(missao != null){
            System.out.println("Missão realizada: " + missao);
        }
        else{
            System.out.println("Não há missões na fila.");
        }
    }
}
