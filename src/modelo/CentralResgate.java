package modelo;

import java.util.*;
import enums.StatusRobo;
import enums.TipoTerreno;
import excecoes.MissaoInvalidaException;
import java.io.Serializable;

public class CentralResgate implements Serializable{
    public String nome;
    public Queue<Missao> missoesPendentes;
    public ArrayList<Robo> robos;
    public HashMap<Integer, AreaOperacao> areasOperacao;
    public Set<Missao> missoes;
    //getters e setters
    public String getNome(){
        return this.nome;
    }
    public Queue<Missao> getMissoesPendentes(){
        return this.missoesPendentes;
    }
    public ArrayList<Robo> getRobos(){
        return this.robos;
    }
    public HashMap<Integer, AreaOperacao> getAreasOperacao(){
        return this.areasOperacao;
    }
    public Set<Missao> getMissoes(){
        return this.missoes;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setMissoesPendentes(Queue<Missao> missoesPendentes){
        this.missoesPendentes = missoesPendentes;
    }
    public void setRobos(ArrayList<Robo> robos){
        this.robos = robos;
    }
    public void setAreasOperacao(HashMap<Integer, AreaOperacao> areasOperacao){
        this.areasOperacao = areasOperacao;
    }
    public void setMissoes(Set<Missao> missoes){
        this.missoes = missoes;
    }
    //--------------\\
    public CentralResgate(String nome){
        this.nome = nome;
        this.missoesPendentes = new LinkedList<Missao>();
        this.robos = new ArrayList<Robo>();
        this.areasOperacao = new HashMap<Integer, AreaOperacao>();
        this.missoes = new HashSet<Missao>(0);
    }
    public void listarMissoesPendentes(){
        System.out.println("Missões Pendentes:");
        for(Missao missao : this.missoesPendentes){
            System.out.println("Id: "+missao.getIdMissao()+" Tipo: "+missao.getTipo()+" Prioridade: "+missao.getPrioridade()+" Status: "+missao.getStatus());
        }
    }
    public void cadastrarRobo(Robo robo){
        this.robos.add(robo);
    }
    public void cadastrarArea(int id, AreaOperacao area){
        this.areasOperacao.put(id, area);
    }
    public void registrarMissao(Missao missao){
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
            System.out.println("Tipo de Terreno: "+this.areasOperacao.get(id).getTipoTerreno());
        }
        else{
            System.out.println("Área de Operação não encontrada");
        }
    }
    public boolean atribuirRoboMissao(Robo robo, Missao missao) throws MissaoInvalidaException{
        if(this.robos.contains(robo) && this.robos.get(this.robos.indexOf(robo)).verificarDisponibilidade() == StatusRobo.DISPONIVEL && this.missoesPendentes.contains(missao) && this.robos.get(this.robos.indexOf(robo)).verificarCompatibilidadeArea(missao.getArea()) && this.robos.get(this.robos.indexOf(robo)).checarTipoMissao(missao)){
            missao.atribuirRobo(robo);
            missao.iniciar();
            this.robos.get(this.robos.indexOf(robo)).setStatus(StatusRobo.EM_MISSAO);
            return true;
        }
        return false;
    }
    public void finalizarMissao(Missao missao, String resultado) throws MissaoInvalidaException{
        if(this.robos.contains(missao.getRoboResponsavel()) && this.robos.get(this.robos.indexOf(missao.getRoboResponsavel())).verificarDisponibilidade() == StatusRobo.EM_MISSAO && this.missoesPendentes.contains(missao) && this.robos.get(this.robos.indexOf(missao.getRoboResponsavel())).executarMissao(missao) && this.robos.get(this.robos.indexOf(missao.getRoboResponsavel())).verificarCompatibilidadeArea(missao.getArea())){
            missao.concluir(resultado);
            missao.getRoboResponsavel().executarMissao(missao);
        }
    }
}
