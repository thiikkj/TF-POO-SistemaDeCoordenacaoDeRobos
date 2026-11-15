package modelo;

import java.util.*;
import enums.StatusRobo;
import enums.TipoTerreno;

public class CentralResgate {
    public String nome;
    public Queue<Missao> missoesPendentes;
    public ArrayList<Robo> robos;
    public HashMap<Integer, TipoTerreno> areasOperacao;
    public Set<Missao> missoes;
    //--------------\\
    public CentralResgate(String nome){
        this.nome = nome;
        this.missoesPendentes = new LinkedList<Missao>();
        this.robos = new ArrayList<Robo>();
        this.areasOperacao = new HashMap<Integer, TipoTerreno>();
        this.missoes = new HashSet<Missao>(0);
    }
    public void cadastrarRobo(Robo robo){
        this.robos.add(robo);
    }
    public void cadastrarArea(int id, TipoTerreno terreno){
        this.areasOperacao.put(id, terreno);
    }
    public void adicionarMissao(Missao missao){
        this.missoes.add(missao);
        if(missao.getStatus() == enums.StatusMissao.PENDENTE){
            this.missoesPendentes.add(missao);
        }
    }
    public void removerMissao(Missao missao){
        this.missoes.remove(missao);
        this.missoesPendentes.remove(missao);
    }
    public void removerRobo(Robo robo){
        this.robos.remove(robo);
    }
    public void removerAreaOperacao(int id){
        this.areasOperacao.remove(id);
    }
    public void exibirResumo(){
        System.out.println("Nome da Central: "+this.nome);
        System.out.println("Quantidade de Robôs: "+this.robos.size());
        System.out.println("Quantidade de Áreas de Operação: "+this.areasOperacao.size());
        System.out.println("Quantidade de Missões: "+this.missoes.size());
        System.out.println("Quantidade de Missões Pendentes: "+this.missoesPendentes.size());
    }
    public void exibirResumoRobos(){
        System.out.println("Robôs:");
        for(Robo robo : this.robos){
            System.out.println(robo.exibirResumo());
        }
    }
    public boolean verificarDisponibilidadeRobo(Robo robo){
        if(this.robos.contains(robo)){
            return this.robos.get(this.robos.indexOf(robo)).verificarDisponibilidade() == StatusRobo.DISPONIVEL;
        }
        return false;
    }
    public int consultarBateriaRobo(Robo robo){
        if(this.robos.contains(robo)){
            return this.robos.get(this.robos.indexOf(robo)).consultarBateria();
        }
        return -1;
    }
    public String recarregarRobo(Robo robo){
        if(this.robos.contains(robo)){
            return this.robos.get(this.robos.indexOf(robo)).recarregar();
        }
        return "Robo não encontrado";
    }
    public void enviarMensagemRobo(Robo robo, String mensagem){
        if(this.robos.contains(robo)){
            this.robos.get(this.robos.indexOf(robo)).enviarMensagem(mensagem);
        }
    }
    public void receberMensagemRobo(Robo robo, String mensagem){
        if(this.robos.contains(robo)){
            this.robos.get(this.robos.indexOf(robo)).receberMensagem(mensagem);
        }
    }
    public void exibirResumoMissao(Missao missao){
        System.out.println("Missão:");
        System.out.println("Id: "+missao.getIdMissao());
        System.out.println("Tipo: "+missao.getTipo());
        System.out.println("Prioridade: "+missao.getPrioridade());
        System.out.println("Status: "+missao.getStatus());
        if(missao.getRoboResponsavel() != null){
            System.out.println("Robo Atribuído: "+missao.getRoboResponsavel().getNome());
        }
        else{
            System.out.println("Robo Atribuído: Nenhum");
        }
    }
    public void exibirResumoAreaOperacao(int id){
        if(this.areasOperacao.containsKey(id)){
            System.out.println("Área de Operação:");
            System.out.println("Id: "+id);
            System.out.println("Tipo de Terreno: "+this.areasOperacao.get(id));
        }
        else{
            System.out.println("Área de Operação não encontrada");
        }
    }
    public boolean comecarMissao(Robo robo, Missao missao){
        if(this.robos.contains(robo) && this.robos.get(this.robos.indexOf(robo)).verificarDisponibilidade() == StatusRobo.DISPONIVEL && this.missoesPendentes.contains(missao) && this.robos.get(this.robos.indexOf(robo)).executarMissao(missao) && this.robos.get(this.robos.indexOf(robo)).verificarCompatibilidadeArea(missao.getArea())){
            missao.atribuirRobo(robo);
            missao.setStatus(enums.StatusMissao.EM_ANDAMENTO);
            this.robos.get(this.robos.indexOf(robo)).setStatus(StatusRobo.EM_MISSAO);
            return true;
        }
        return false;
    }
    
}
