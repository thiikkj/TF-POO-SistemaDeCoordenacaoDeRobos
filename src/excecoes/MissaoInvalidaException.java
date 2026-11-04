package excecoes;

//Exceção usada quando uma operação não é permitida para o estado atual da missão.
//Exemplo: tentar iniciar de novo uma missão que já foi concluída.
public class MissaoInvalidaException extends Exception {
   
    //Recebe o texto do erro (mensagem) e o entrega para a classe pai (Exception) guardar.
    public MissaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}