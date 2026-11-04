package excecoes;

//Exceção usada quando alguém tenta cadastrar ou alterar algo com dados inválidos.
//Exemplo: cadastrar um robô com 150% de bateria, sendo que o limite é 100%.
public class DadosInvalidosException extends Exception {

    //Recebe o texto do erro (mensagem) e o entrega para a classe pai (Exception) guardar.
    public DadosInvalidosException(String mensagem) {
        super(mensagem);
    }
}