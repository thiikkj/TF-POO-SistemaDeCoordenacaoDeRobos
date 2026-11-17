package modelo;

import java.util.*;
import enums.PrioridadeMissao;
import enums.StatusMissao;
import enums.StatusRobo;
import excecoes.MissaoInvalidaException;
import java.io.Serializable;

//Classe que coordena tudo: guarda os robôs, as áreas e as missões, e distribui as missões entre os robôs.
//Lembrete: Os atributos são private, então as outras classes só acessam eles pelos getters e setters (encapsulamento).
public class CentralResgate implements Serializable{

    //Fixando a versão da classe em 1L, os arquivos salvos continuam abrindo mesmo depois de mudarmos a classe.
    private static final long serialVersionUID = 1L;

    private String nome;
    //A fila guarda as missões que ainda não terminaram (pendentes e em andamento), na ordem em que chegaram.
    private Queue<Missao> missoesPendentes;
    private ArrayList<Robo> robos;
    private HashMap<Integer, AreaOperacao> areasOperacao;
    private Set<Missao> missoes;
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

    //Confere se o robô pode receber a missão agora: disponível, faz esse tipo de missão, atua no terreno da área e tem bateria.
    private boolean podeAssumir(Robo robo, Missao missao){
        return robo.verificarDisponibilidade() == StatusRobo.DISPONIVEL
                && robo.checarTipoMissao(missao)
                && robo.verificarCompatibilidadeArea(missao.getArea())
                && robo.checarBateriaMinima(missao);
    }

    //Funcionalidade inovadora: as missões de prioridade ALTA que ainda estão PENDENTES recebem um robô automaticamente.
    //Entre os robôs que podem assumir a missão, a central escolhe o que vai ficar com mais bateria depois dela,
    //assim sobra mais carga na equipe para as próximas emergências. Em caso de empate, fica o que foi cadastrado primeiro.
    public void atribuirMissoesUrgentes(){
        for(Missao missao : this.missoesPendentes){
            if(missao.getPrioridade() == PrioridadeMissao.ALTA && missao.getStatus() == StatusMissao.PENDENTE){
                Robo escolhido = null;
                for(Robo robo : this.robos){
                    if(podeAssumir(robo, missao)){
                        if(escolhido == null || robo.preverBateriaAposMissao(missao) > escolhido.preverBateriaAposMissao(missao)){
                            escolhido = robo;
                        }
                    }
                }
                if(escolhido != null){
                    try{
                        this.atribuirRoboMissao(escolhido, missao);
                    }
                    catch(MissaoInvalidaException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    //Funcionalidade inovadora: antes de mandar um robô, mostra quanto de bateria cada robô gastaria na missão
    //e com quanto ficaria depois, para a central saber quem pode ir sem ficar sem carga no meio do resgate.
    public void exibirBateriaNecessaria(Missao missao){
        System.out.println("Bateria necessária para a missão #"+missao.getIdMissao()+" ("+missao.getTipo()+", prioridade "+missao.getPrioridade()+", área "+missao.getArea().getNomeArea()+"):");
        for(Robo robo : this.robos){
            String linha = "- "+robo.getNome()+" (bateria atual: "+robo.getNivelBateria()+"%): ";
            int necessaria = robo.calcularBateriaNecessaria(missao);
            if(necessaria < 0){
                linha += "não realiza missões de "+missao.getTipo()+".";
            }
            else if(!robo.verificarCompatibilidadeArea(missao.getArea())){
                linha += "não atua no terreno "+missao.getArea().getTipoTerreno()+".";
            }
            else if(!robo.checarBateriaMinima(missao)){
                linha += "precisa de "+necessaria+"%, falta bateria (recarregar antes).";
            }
            else{
                linha += "gasta "+necessaria+"% e fica com "+robo.preverBateriaAposMissao(missao)+"%";
                if(robo.verificarDisponibilidade() != StatusRobo.DISPONIVEL){
                    linha += ", mas agora está "+robo.getStatus();
                }
                linha += ".";
            }
            System.out.println(linha);
        }
    }

    //Mostra só as missões que ainda esperam um robô. As que já começaram continuam na fila, mas não aparecem aqui.
    public void listarMissoesPendentes(){
        System.out.println("Missões Pendentes:");
        for(Missao missao : this.missoesPendentes){
            if(missao.getStatus() == StatusMissao.PENDENTE){
                System.out.println("Id: "+missao.getIdMissao()+" Tipo: "+missao.getTipo()+" Prioridade: "+missao.getPrioridade()+" Status: "+missao.getStatus());
            }
        }
    }
    public void listarRobosDisponiveis(){
        System.out.println("Robôs Disponíveis:");
        for(Robo robo : this.robos){
            if(robo.getStatus() == StatusRobo.DISPONIVEL){
                System.out.println("Id: "+robo.getId()+" Nome: "+robo.getNome()+" Status: "+robo.getStatus()+" Bateria: "+robo.getNivelBateria());
            }
        }
    }
    //Regra do id único: compara o id, e não o objeto, porque dois objetos diferentes podem ter o mesmo id.
    public boolean cadastrarRobo(Robo robo){
        if(robo == null){
            return false;
        }
        for(Robo r : this.robos){
            if(r.getId() == robo.getId()){
                return false;
            }
        }
        this.robos.add(robo);
        return true;
    }
    public boolean cadastrarArea(Integer id, AreaOperacao area){
        if(areasOperacao.containsKey(id)){
            return false;
        }
        this.areasOperacao.put(id, area);
        return true;
    }
    //Mesma regra do id único, agora para as missões.
    public boolean registrarMissao(Missao missao){
        if(missao == null){
            return false;
        }
        for(Missao m : this.missoes){
            if(m.getIdMissao() == missao.getIdMissao()){
                return false;
            }
        }
        this.missoes.add(missao);
        if(missao.getStatus() == StatusMissao.PENDENTE){
            this.missoesPendentes.add(missao);
        }
        return true;
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
        int pendentes = 0;
        for(Missao missao : this.missoesPendentes){
            if(missao.getStatus() == StatusMissao.PENDENTE){
                pendentes++;
            }
        }
        System.out.println("Nome da Central: "+this.nome);
        System.out.println("Quantidade de Robôs: "+this.robos.size());
        System.out.println("Quantidade de Áreas de Operação: "+this.areasOperacao.size());
        System.out.println("Quantidade de Missões: "+this.missoes.size());
        System.out.println("Quantidade de Missões Pendentes: "+pendentes);
    }
    public void exibirResumoRobos(){
        System.out.println("Robôs:");
        for(Robo robo : this.robos){
            System.out.println(robo.exibirResumo());
        }
    }
    public boolean verificarDisponibilidadeRobo(Robo robo){
        if(this.robos.contains(robo)){
            return robo.verificarDisponibilidade() == StatusRobo.DISPONIVEL;
        }
        return false;
    }
    public int consultarBateriaRobo(Robo robo){
        if(this.robos.contains(robo)){
            return robo.consultarBateria();
        }
        return -1;
    }
    public String recarregarRobo(Robo robo){
        if(this.robos.contains(robo)){
            return robo.recarregar();
        }
        return "Robo não encontrado";
    }
    public void enviarMensagemRobo(Robo robo, String mensagem){
        if(this.robos.contains(robo)){
            robo.enviarMensagem(mensagem);
        }
    }
    public void receberMensagemRobo(Robo robo, String mensagem){
        if(this.robos.contains(robo)){
            robo.receberMensagem(mensagem);
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
    //Liga o robô à missão e já inicia ela. Devolve false se alguma regra impedir (missão não pendente, robô ocupado,
    //tipo de missão ou terreno incompatível, ou bateria insuficiente).
    public boolean atribuirRoboMissao(Robo robo, Missao missao) throws MissaoInvalidaException{
        if(this.robos.contains(robo) && this.missoesPendentes.contains(missao) && missao.getStatus() == StatusMissao.PENDENTE && podeAssumir(robo, missao)){
            missao.atribuirRobo(robo);
            missao.iniciar();
            robo.setStatus(StatusRobo.EM_MISSAO);
            return true;
        }
        return false;
    }
    //Conclui a missão: o robô gasta a bateria da missão, volta a ficar disponível e a missão sai da fila.
    //Se a missão não puder ser finalizada, lança MissaoInvalidaException dizendo o motivo.
    public void finalizarMissao(Missao missao, String resultado) throws MissaoInvalidaException{
        if(missao == null){
            throw new MissaoInvalidaException("ERRO - A missão é obrigatória e não pode ser nula.");
        }
        if(missao.getStatus() != StatusMissao.EM_ANDAMENTO || !this.missoesPendentes.contains(missao)){
            throw new MissaoInvalidaException("ERRO - Só é possível finalizar uma missão que está em andamento. A missão #"+missao.getIdMissao()+" está "+missao.getStatus()+".");
        }
        Robo robo = missao.getRoboResponsavel();
        if(robo == null || !this.robos.contains(robo)){
            throw new MissaoInvalidaException("ERRO - A missão #"+missao.getIdMissao()+" não tem um robô da central responsável por ela.");
        }
        if(robo.verificarDisponibilidade() != StatusRobo.EM_MISSAO){
            throw new MissaoInvalidaException("ERRO - O robô "+robo.getNome()+" não está em missão (status atual: "+robo.getStatus()+").");
        }
        if(!robo.checarBateriaMinima(missao)){
            throw new MissaoInvalidaException("ERRO - O robô "+robo.getNome()+" não tem bateria suficiente para terminar a missão (bateria atual: "+robo.getNivelBateria()+"%).");
        }
        missao.concluir(resultado);
        robo.executarMissao(missao);
        this.missoesPendentes.remove(missao);
    }
    public Relatorio gerarRelatorioGeral(){
        return new Relatorio(this);
    }
}
