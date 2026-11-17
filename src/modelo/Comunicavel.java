package modelo;

//A interface Comunicavel não escreve o que esses métodos fazem, só obriga a existirem.
//Quem implementar a interface "Comunicavel" é obrigado a escrever os dois métodos abaixo, senão o código não compila.
public interface Comunicavel {
    
    //Lembrete: Todos os metodos de uma interface são "public" e "abstract" por padrão, então não precisa escrever "public abstract" antes do método.

    //Envia uma mensagem do robô para a central.
    void enviarMensagem(String mensagem);

    //Recebe uma mensagem enviada pela central.
    void receberMensagem(String mensagem);
}