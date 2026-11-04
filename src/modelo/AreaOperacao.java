package modelo;
//Imports
import java.io.Serializable;
import enums.TipoTerreno;
import enums.NivelRisco;
import excecoes.DadosInvalidosException;

//Essa classe representa uma área onde as missões de resgate acontecem.
//Lembrete: O "implements Serializable" permite que os objetos criados a partir dessa classe possam ser salvos em arquivos.
public class AreaOperacao implements Serializable {
    
    //Número de versão da classe, salvo junto com o objeto no arquivo.
    //Fixando a versão da classe em 1L, os objetos salvos antes continuam abrindo mesmo depois de adicionarmos atributos ou métodos na classe.
    private static final long serialVersionUID = 1L;

    //Atributos
    private int idArea;
    private String nomeArea;
    private String localizacao;
    private TipoTerreno tipoTerreno;
    private NivelRisco nivelRisco;
    private String descricao;

    //Construtor - Valida os dados recebidos e se algum dado for inválido, lança DadosInvalidosException.
    public AreaOperacao(int idArea, String nomeArea, String localizacao, TipoTerreno tipoTerreno, NivelRisco nivelRisco, String descricao) throws DadosInvalidosException {
        
        //Verifica idArea
        if (idArea <= 0) {
            throw new DadosInvalidosException("ERRO - O ID da área deve ser maior que zero.");
        }

        //Verifica nomeArea
        if (nomeArea == null || nomeArea.trim().isEmpty()) {
            throw new DadosInvalidosException("ERRO - O nome da área é obrigatório e não pode ser vazio.");
        }

        //Verifica localizacao
        if (localizacao == null || localizacao.trim().isEmpty()) {
            throw new DadosInvalidosException("ERRO - A localização da área é obrigatória e não pode ser vazia.");
        }

        //Verifica tipoTerreno
        if (tipoTerreno == null) {
            throw new DadosInvalidosException("ERRO - O tipo de terreno da área é obrigatório e não pode ser nulo.");
        }

        //Verifica nivelRisco
        if (nivelRisco == null) {
            throw new DadosInvalidosException("ERRO - O nível de risco da área é obrigatório e não pode ser nulo.");
        }

        //Se passou por todas as validações, atribui os valores aos atributos.
        this.idArea = idArea;
        this.nomeArea = nomeArea;
        this.localizacao = localizacao;
        this.tipoTerreno = tipoTerreno;
        this.nivelRisco = nivelRisco;
        //Isso serve para não aparecer a palavra null na descrição caso o usuário não tenha informado nada, já que a descrição é opcional.
        this.descricao = (descricao == null) ? "" : descricao;
    }

    //Metodos Getters
    //Lembrete: Os Getters nos permitem acessar os valores dos atributos privados da classe em outra classe.
    public int getIdArea() {
        return idArea;
    }

    public String getNomeArea() {
        return nomeArea;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public TipoTerreno getTipoTerreno() {
        return tipoTerreno;
    }

    public NivelRisco getNivelRisco() {
        return nivelRisco;
    }
    
    public String getDescricao() {
        return descricao;
    }


    //Metodo que atualiza o nível de risco da área durante uma operação de resgate. Se o novo nível de risco for nulo, lança DadosInvalidosException.
    public void atualizarNivelRisco(NivelRisco novoNivelRisco) throws DadosInvalidosException {
        if (novoNivelRisco == null) {
            throw new DadosInvalidosException("ERRO - O novo nível de risco é obrigatório e não pode ser nulo.");
        }
        this.nivelRisco = novoNivelRisco;
    }

    //Metodo que mostra as informações da área
    public void exibirInformacoes() {
        System.out.println("Área #" + idArea + " - " + nomeArea);
        System.out.println("Localização: " + localizacao);
        System.out.println("Tipo de Terreno: " + tipoTerreno);
        System.out.println("Nível de Risco: " + nivelRisco);
        System.out.println("Descrição: " + descricao);
    }
}