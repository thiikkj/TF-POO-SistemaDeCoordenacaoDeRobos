package modelo;
//imports
import java.util.*;
import java.io.Serializable;
import enums.PrioridadeMissao;
import enums.StatusRobo;
import excecoes.DadosInvalidosException;

//Classe abstrata com o que todo robô tem em comum. Drone e RoboTerrestre herdam dela.
//Lembrete: Os atributos são private, então as subclasses e as outras classes só acessam eles pelos getters e setters (encapsulamento).
public abstract class Robo implements Comunicavel, Serializable{

    //Fixando a versão da classe em 1L, os arquivos salvos continuam abrindo mesmo depois de mudarmos a classe.
    private static final long serialVersionUID = 1L;

    //Limites da bateria, em porcentagem.
    public static final int BATERIA_MINIMA = 0;
    public static final int BATERIA_MAXIMA = 100;

    private int id;
    private String nome;
    private int nivelBateria;
    private StatusRobo status;
    private ArrayList<Missao> missoesRealizadas;

    public Robo(int id, String nome, StatusRobo status){
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.nivelBateria = BATERIA_MAXIMA;
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
    //Devolve uma cópia, assim quem chamar não consegue mexer na lista original do robô.
    public List<Missao> getMissoesRealizadas(){
        return new ArrayList<Missao>(this.missoesRealizadas);
    }
    //A bateria é uma porcentagem, então só aceita valores de 0 a 100.
    public void setNivelBateria(int bateria) throws DadosInvalidosException{
        if(bateria < BATERIA_MINIMA || bateria > BATERIA_MAXIMA){
            throw new DadosInvalidosException("ERRO - O nível de bateria deve estar entre "+BATERIA_MINIMA+" e "+BATERIA_MAXIMA+". Valor informado: "+bateria+".");
        }
        this.nivelBateria = bateria;
    }
    public void setStatus(StatusRobo status){
        this.status = status;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    //-------------------------------\\
    protected StatusRobo verificarDisponibilidade(){
        return this.status;
    }
    protected int consultarBateria(){
        return this.nivelBateria;
    }
    protected String recarregar(){
        this.nivelBateria = BATERIA_MAXIMA;
        if(this.status == StatusRobo.SEM_BATERIA){
            setStatus(StatusRobo.DISPONIVEL);
        }
        return "Bateria Recarregada";
    }

    //---------------------- Bateria das missões (funcionalidade inovadora) ----------------------

    //Custo base da missão, que depende do tipo de robô. Cada subclasse diz quanto gasta em cada tipo de missão que realiza.
    //Lembrete: Se o robô não realiza aquele tipo de missão, a subclasse devolve -1.
    protected abstract int custoBase(Missao missao);

    //Quanto mais urgente a missão, mais rápido o robô precisa ir, e mais bateria ele gasta.
    protected int adicionalPrioridade(Missao missao){
        if(missao.getPrioridade() == PrioridadeMissao.ALTA){
            return 10;
        }
        else if(missao.getPrioridade() == PrioridadeMissao.MEDIA){
            return 5;
        }
        return 0;
    }

    //Calcula quanto de bateria (em %) o robô gasta para fazer a missão: custo base + adicional da prioridade.
    //Devolve -1 se o robô não realiza esse tipo de missão.
    public int calcularBateriaNecessaria(Missao missao){
        int base = custoBase(missao);
        if(base < 0){
            return -1;
        }
        return base + adicionalPrioridade(missao);
    }

    //Mostra quanto de bateria o robô vai ter depois da missão. Devolve -1 se ele não puder fazer a missão.
    public int preverBateriaAposMissao(Missao missao){
        int necessaria = calcularBateriaNecessaria(missao);
        if(necessaria < 0 || necessaria > this.nivelBateria){
            return -1;
        }
        return this.nivelBateria - necessaria;
    }

    //O robô só aceita a missão se a bateria atual cobrir todo o gasto dela.
    public boolean checarBateriaMinima(Missao missao){
        int necessaria = calcularBateriaNecessaria(missao);
        return necessaria >= 0 && this.nivelBateria >= necessaria;
    }

    //Desconta a bateria gasta, guarda a missão no histórico e libera o robô.
    //Se a bateria chegar a 0, o robô fica SEM_BATERIA até ser recarregado.
    public boolean executarMissao(Missao missao){
        if(!checarTipoMissao(missao) || !checarBateriaMinima(missao)){
            return false;
        }
        this.nivelBateria -= calcularBateriaNecessaria(missao);
        this.missoesRealizadas.add(missao);
        if(this.nivelBateria == BATERIA_MINIMA){
            setStatus(StatusRobo.SEM_BATERIA);
        }
        else{
            setStatus(StatusRobo.DISPONIVEL);
        }
        return true;
    }

    //O tipo de missão aceito é o mesmo que tem custo base: se a subclasse sabe o custo, ela realiza a missão.
    public boolean checarTipoMissao(Missao missao){
        return custoBase(missao) >= 0;
    }

    //-------------------------------\\
    public abstract boolean verificarCompatibilidadeArea(AreaOperacao area);
    public abstract String exibirResumo();
    public void enviarMensagem(String mensagem){
        System.out.println("Mensagem enviada: "+mensagem);
    }
    public void receberMensagem(String mensagem){
        System.out.println("Mensagem recebida: "+mensagem);
    }
    //Parte do resumo que é igual para todos os robôs. As subclasses completam com os atributos delas.
    protected String resumoBase(){
        return "Id: "+this.id+"\n"+"Nome: "+this.nome+"\n"+"Bateria: "+this.nivelBateria+"\n"+"Status: "+this.status+"\n"+"Quantidade de Missões: "+this.missoesRealizadas.size();
    }
    public boolean concertar(){
        if(this.status == StatusRobo.EM_MANUTENCAO){
            this.status = StatusRobo.DISPONIVEL;
            this.nivelBateria = BATERIA_MAXIMA;
            System.out.println("Robo "+this.nome+" foi concertado");
            return true;
        }
        System.out.println("Robo "+this.nome+" não estava danificado");
        return false;
    }

}
