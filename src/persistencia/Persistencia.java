package persistencia;
//Imports
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import modelo.CentralResgate;
import excecoes.PersistenciaException;

//Essa classe salva e carrega todos os dados do sistema em um arquivo.
//Lembrete: A CentralResgate guarda os robôs, as áreas e as missões, então salvando a central salvamos tudo de uma vez.
//Lembrete: Isso só funciona porque CentralResgate, Robo, Missao e AreaOperacao têm o "implements Serializable".
public class Persistencia {

    //Construtor privado - Ninguém precisa criar um objeto Persistencia, já que todos os métodos são static.
    //Exemplo de uso: Persistencia.salvar(central, "dados/central.dat");
    private Persistencia() {
    }

    //Metodo que grava a central inteira em um arquivo binário.
    //Se der erro ao gravar, lança PersistenciaException.
    public static void salvar(CentralResgate central, String caminho) throws PersistenciaException {

        //Verifica central
        if (central == null) {
            throw new PersistenciaException("ERRO - Não há nenhuma central para salvar.");
        }

        //Verifica caminho
        if (caminho == null || caminho.trim().isEmpty()) {
            throw new PersistenciaException("ERRO - O caminho do arquivo é obrigatório e não pode ser vazio.");
        }

        //Se a pasta do arquivo (ex: "dados") ainda não existir, cria ela antes de gravar.
        File arquivo = new File(caminho);
        File pasta = arquivo.getParentFile();
        if (pasta != null && !pasta.exists() && !pasta.mkdirs()) {
            throw new PersistenciaException("ERRO - Não foi possível criar a pasta " + pasta.getPath() + ".");
        }

        //O ObjectOutputStream transforma o objeto em bytes e escreve no arquivo.
        //Ele salva junto tudo o que a central guarda: as listas de robôs, o mapa de áreas, o conjunto de missões e a fila de pendentes.
        //O try-with-resources (o parêntese depois do try) fecha o arquivo sozinho no final, mesmo se der erro.
        try (ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            saida.writeObject(central);
        } catch (IOException e) {
            throw new PersistenciaException("ERRO - Não foi possível salvar os dados em " + caminho + ".");
        }
    }

    //Metodo que lê o arquivo e devolve a central do jeito que ela estava quando foi salva.
    //Se o arquivo não existir ou não puder ser lido, lança PersistenciaException.
    public static CentralResgate carregar(String caminho) throws PersistenciaException {

        //Verifica caminho
        if (caminho == null || caminho.trim().isEmpty()) {
            throw new PersistenciaException("ERRO - O caminho do arquivo é obrigatório e não pode ser vazio.");
        }

        //Verifica se o arquivo existe
        if (!existeArquivo(caminho)) {
            throw new PersistenciaException("ERRO - O arquivo " + caminho + " não existe. Salve os dados antes de carregar.");
        }

        //O ObjectInputStream faz o caminho contrário: lê os bytes e monta os objetos de novo.
        //O readObject() devolve um Object, então precisamos converter para CentralResgate (cast).
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(caminho))) {
            return (CentralResgate) entrada.readObject();
        } catch (InvalidClassException e) {
            //Acontece quando o arquivo foi salvo antes de alguma classe ser alterada (a versão dela mudou).
            throw new PersistenciaException("ERRO - O arquivo " + caminho + " foi salvo com uma versão antiga do sistema. Salve os dados de novo.");
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            //Lembrete: O "|" junta várias exceções no mesmo catch, já que o tratamento é igual para as três.
            throw new PersistenciaException("ERRO - Não foi possível carregar os dados de " + caminho + ". O arquivo pode estar corrompido.");
        }
    }

    //Metodo que diz se já existe um arquivo salvo no caminho informado.
    public static boolean existeArquivo(String caminho) {
        File arquivo = new File(caminho);
        return arquivo.exists() && arquivo.isFile();
    }
}
