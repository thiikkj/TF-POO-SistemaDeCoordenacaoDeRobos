package modelo;

import enums.StatusRobo;
import enums.TipoMissao;
import enums.TipoTerreno;
import excecoes.DadosInvalidosException;

//Robô que anda pelo chão. Faz localização de pessoas e transporte de suprimentos, mas só no tipo de terreno em que atua.
public class RoboTerrestre extends Robo {

    private static final long serialVersionUID = 1L;

    private int velocidadeMax;
    private TipoTerreno terreno;
    private double cargaMax;
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

    public RoboTerrestre(int id, String nome, StatusRobo status, int velocidadeMax, TipoTerreno terreno, double cargaMax) throws DadosInvalidosException{
        super(id, nome, status);
        if(velocidadeMax <= 0){
            throw new DadosInvalidosException("ERRO - A velocidade máxima deve ser maior que zero.");
        }
        if(terreno == null){
            throw new DadosInvalidosException("ERRO - O tipo de terreno do robô é obrigatório e não pode ser nulo.");
        }
        if(cargaMax <= 0){
            throw new DadosInvalidosException("ERRO - A carga máxima deve ser maior que zero.");
        }
        this.velocidadeMax = velocidadeMax;
        this.terreno = terreno;
        this.cargaMax = cargaMax;
    }

    //Custo base do robô terrestre: 40% na localização de pessoas e 50% no transporte de suprimentos. Não faz reconhecimento (-1).
    @Override
    protected int custoBase(Missao missao){
        if(missao.getTipo() == TipoMissao.LOCALIZACAO_PESSOAS){
            return 40;
        }
        else if(missao.getTipo() == TipoMissao.TRANSPORTE_SUPRIMENTOS){
            return 50;
        }
        return -1;
    }
    //O robô terrestre vai por terra até a área e avisa a central pela interface Comunicavel. No transporte, informa a carga que consegue levar.
    @Override
    public boolean executarMissao(Missao missao){
        if(!consumirBateria(missao)){
            return false;
        }
        if(missao.getTipo() == TipoMissao.TRANSPORTE_SUPRIMENTOS){
            enviarMensagem("Robô "+getNome()+" levou os suprimentos até a área "+missao.getArea().getNomeArea()+" (carga máxima: "+this.cargaMax+" kg).");
        }
        else{
            enviarMensagem("Robô "+getNome()+" percorreu a área "+missao.getArea().getNomeArea()+" por terra.");
        }
        return true;
    }
    @Override
    public String exibirResumo(){
        return resumoBase()+"\n"+"Velocidade Máxima: "+this.velocidadeMax+"\n"+"Terreno: "+this.terreno+"\n"+"Carga Máxima: "+this.cargaMax;
    }
    //Só entra em áreas do mesmo tipo de terreno em que ele atua.
    @Override
    public boolean verificarCompatibilidadeArea(AreaOperacao area){
        return area.getTipoTerreno() == this.terreno;
    }
}
