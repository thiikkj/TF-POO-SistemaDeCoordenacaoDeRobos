package excecoes;

//Exceção usada quando acontece um erro ao salvar ou carregar dados em arquivo.
//Exemplo: o programa tenta carregar os dados salvos, mas o arquivo foi apagado.
public class PersistenciaException extends Exception{
    
    //Recebe o texto do erro (mensagem) e o entrega para a classe pai (Exception) guardar.
    public PersistenciaException(String mensagem) {
        super(mensagem);
    }
}
