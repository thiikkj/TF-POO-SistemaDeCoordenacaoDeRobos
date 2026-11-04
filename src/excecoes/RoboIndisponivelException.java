package excecoes;

//Exceção usada quando não é possível usar um robô para uma missão.
//Exemplo: tentar mandar um robô que já está executando outra missão ou que está sem bateria.
public class RoboIndisponivelException extends Exception {
    
    //Recebe o texto do erro (mensagem) e o entrega para a classe pai (Exception) guardar.
    public RoboIndisponivelException(String mensagem) {
        super(mensagem);
    }
}