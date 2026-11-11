package modelo;
//Imports
import java.io.Serializable;
import enums.PrioridadeMissao;
import enums.StatusMissao;
import enums.TipoMissao;
import excecoes.DadosInvalidosException;
import excecoes.MissaoInvalidaException;

//Essa classe representa uma atividade que um robô vai realizar em uma área de operação.
//Lembrete: O "implements Serializable" permite que os objetos criados a partir dessa classe possam ser salvos em arquivos.
public class Missao implements Serializable {

    //Número de versão da classe, salvo junto com o objeto no arquivo.
    //Fixando a versão da classe em 1L, os objetos salvos antes continuam abrindo mesmo depois de adicionarmos atributos ou métodos na classe.
    private static final long serialVersionUID = 1L;

    //Atributos
    private int idMissao;
    private String descricao;
    private TipoMissao tipoMissao;
    private PrioridadeMissao prioridadeMissao;
    private AreaOperacao areaOperacao;
    private StatusMissao statusMissao;
    private Robo roboResponsavel;
    private String resultado;

    //Construtor - Valida os dados recebidos e se algum dado for inválido, lança DadosInvalidosException.
    public Missao(int idMissao, String descricao, TipoMissao tipoMissao, PrioridadeMissao prioridadeMissao, AreaOperacao areaOperacao) throws DadosInvalidosException {

        //Verifica idMissao
        if (idMissao <= 0) {
            throw new DadosInvalidosException("ERRO - O ID da missão deve ser maior que zero.");
        }

        //Verifica descricao
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new DadosInvalidosException("ERRO - A descrição da missão é obrigatória e não pode ser vazia.");
        }

        //Verifica tipo
        if (tipoMissao == null) {
            throw new DadosInvalidosException("ERRO - O tipo da missão é obrigatório e não pode ser nulo.");
        }

        //Verifica prioridade
        if (prioridadeMissao == null) {
            throw new DadosInvalidosException("ERRO - A prioridade da missão é obrigatória e não pode ser nula.");
        }

        //Verifica area
        //REGRA 5: toda missão precisa estar ligada a uma área de operação
        if (areaOperacao == null) {
            throw new DadosInvalidosException("ERRO - A missão precisa estar associada a uma área de operação válida.");
        }

        //Se passou por todas as validações, atribui os valores aos atributos.
        this.idMissao = idMissao;
        this.descricao = descricao;
        this.tipoMissao = tipoMissao;
        this.prioridadeMissao = prioridadeMissao;
        this.areaOperacao = areaOperacao;

        //Toda missão nova começa pendente, sem robô e sem resultado.
        this.statusMissao = StatusMissao.PENDENTE;
        this.roboResponsavel = null;
        this.resultado = "";
    }

    //Metodos Getters
    //Lembrete: Os Getters nos permitem acessar os valores dos atributos privados da classe em outra classe.
    public int getIdMissao() {
        return idMissao;
    }

    public String getDescricao() {
        return descricao;
    }

    public TipoMissao getTipo() {
        return tipoMissao;
    }

    public PrioridadeMissao getPrioridade() {
        return prioridadeMissao;
    }

    public AreaOperacao getArea() {
        return areaOperacao;
    }

    public StatusMissao getStatus() {
        return statusMissao;
    }

    public Robo getRoboResponsavel() {
        return roboResponsavel;
    }

    public String getResultado() {
        return resultado;
    }


    //Metodo que liga um robô à missão. Só dá para atribuir enquanto a missão estiver pendente.
    public void atribuirRobo(Robo robo) throws MissaoInvalidaException {
        if (robo == null) {
            throw new MissaoInvalidaException("ERRO - O robô é obrigatório e não pode ser nulo.");
        }

        if (statusMissao != StatusMissao.PENDENTE) {
            throw new MissaoInvalidaException("ERRO - Só é possível atribuir um robô a uma missão pendente.");
        }

        this.roboResponsavel = robo;
    }

    //Metodo que inicia a missão. Muda o status para EM_ANDAMENTO.
    public void iniciarMissao() throws MissaoInvalidaException {

        //REGRA 9: missão concluída ou cancelada não pode ser iniciada de novo
        if (statusMissao == StatusMissao.CONCLUIDA || statusMissao == StatusMissao.CANCELADA) {
            throw new MissaoInvalidaException("ERRO - Uma missão concluída ou cancelada não pode ser iniciada novamente.");
        }

        if (statusMissao == StatusMissao.EM_ANDAMENTO) {
            throw new MissaoInvalidaException("ERRO - A missão já está em andamento.");
        }

        //REGRA 6: a missão só pode ser iniciada depois que um robô foi atribuído
        if (roboResponsavel == null) {
            throw new MissaoInvalidaException("ERRO - A missão só pode ser iniciada depois de atribuir um robô.");
        }

        this.statusMissao = StatusMissao.EM_ANDAMENTO;
    }

    //Metodo que conclui a missão e guarda o resultado. Só dá para concluir uma missão que está em andamento.
    public void concluirMissao(String resultado) throws MissaoInvalidaException {
        if (statusMissao != StatusMissao.EM_ANDAMENTO) {
            throw new MissaoInvalidaException("ERRO - Só é possível concluir uma missão que está em andamento.");
        }

        //Se não vier nenhum resultado, guarda um texto padrão para o campo não ficar vazio.
        if (resultado == null || resultado.trim().isEmpty()) {
            this.resultado = "Missão concluída sem resultado específico.";    
        } else {
            this.resultado = resultado;
        }

        this.statusMissao = StatusMissao.CONCLUIDA;
    }

    //Metodo que cancela a missão. Missão que já terminou ou já foi cancelada não pode ser cancelada.
    public void cancelarMissao() throws MissaoInvalidaException {
        if (statusMissao == StatusMissao.CONCLUIDA || statusMissao == StatusMissao.CANCELADA) {
            throw new MissaoInvalidaException("ERRO - Uma missão concluída ou cancelada não pode ser cancelada novamente.");
        }

        this.statusMissao = StatusMissao.CANCELADA;
    }

    //Metodo que mostra o resumo da missão
    public void exibirResumo() {
        System.out.println("Missão #" + idMissao + " - " + tipoMissao);
        System.out.println("Descrição: " + descricao);
        System.out.println("Área: " + areaOperacao.getNomeArea());
        System.out.println("Prioridade: " + prioridadeMissao);
        System.out.println("Status: " + statusMissao);
        System.out.println("Resultado: " + resultado);

        //Se ainda não tem robô, mostra um texto no lugar para não aparecer "null"
        if (roboResponsavel == null) {
            System.out.println("Robô responsável: Nenhum robô atribuído.");
        } else {
            System.out.println("Robô responsável: " + roboResponsavel.getNome());
        }
    }
}