package robos;
//imports

public abstract class Robo {
    private int id;
    public String nome;
    public int nivelBateria;
    public String status;
    public int missoesRealizadas;

    public void setNivelBateria(int bateria){
        this.nivelBateria = bateria;
    }

    protected String verificarDisponibilidade(){
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
        return "Id: "+this.id+"\n"+"Nome: "+this.nome+"\n"+"Bateria: "+this.nivelBateria+"\n"+"Status: "+this.status+"\n"+"Quantidade de Missões: "+this.missoesRealizadas;
    }
    abstract public void executarMissao();
    
}
