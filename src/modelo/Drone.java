package modelo;

import enums.StatusRobo;
import enums.TipoMissao;
import excecoes.DadosInvalidosException;

//Robô aéreo. Faz reconhecimento e localização de pessoas, e voa sobre qualquer tipo de terreno.
public class Drone extends Robo {

    private static final long serialVersionUID = 1L;

    private boolean imagem;
    private int altitudeMax;
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
    public Drone(int id, String nome, StatusRobo status, boolean imagem, int altitudeMax) throws DadosInvalidosException{
        super(id, nome, status);
        if(altitudeMax <= 0){
            throw new DadosInvalidosException("ERRO - A altitude máxima deve ser maior que zero.");
        }
        this.altitudeMax = altitudeMax;
        this.imagem = imagem;
    }

    //Custo base do drone: 30% no reconhecimento e 40% na localização de pessoas. Não faz transporte (-1).
    @Override
    protected int custoBase(Missao missao){
        if(missao.getTipo() == TipoMissao.RECONHECIMENTO){
            return 30;
        }
        else if(missao.getTipo() == TipoMissao.LOCALIZACAO_PESSOAS){
            return 40;
        }
        return -1;
    }

    //O drone sobrevoa a área e avisa a central pela interface Comunicavel. Se tiver câmera, também manda as imagens do local.
    @Override
    public boolean executarMissao(Missao missao){
        if(!consumirBateria(missao)){
            return false;
        }
        if(this.imagem){
            enviarMensagem("Drone "+getNome()+" sobrevoou a área "+missao.getArea().getNomeArea()+" e enviou as imagens para a central.");
        }
        else{
            enviarMensagem("Drone "+getNome()+" sobrevoou a área "+missao.getArea().getNomeArea()+" (sem câmera, sem imagens).");
        }
        return true;
    }

    //O drone voa, então o tipo de terreno não importa.
    @Override
    public boolean verificarCompatibilidadeArea(AreaOperacao area){
        return true;
    }
    @Override
    public String exibirResumo(){
        return resumoBase()+"\n"+"Imagem: "+this.imagem+"\n"+"Altitude Máxima: "+this.altitudeMax;
    }
}
