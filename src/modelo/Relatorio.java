package modelo;
//Imports
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import enums.StatusMissao;
import excecoes.PersistenciaException;

//Essa classe gera um resumo com os números da central: quantos robôs, quantas missões, quantas concluídas, etc.
public class Relatorio {

    //Atributos
    private LocalDateTime dataGeracao;
    private int totalRobos;
    private int totalMissoes;
    private int missoesConcluidas;
    private int missoesPendentes;
    private int missoesCanceladas;

    //Construtor - Recebe a central e já calcula todos os números do relatório.
    public Relatorio(CentralResgate central) {

        if (central == null) {
            throw new IllegalArgumentException("ERRO - A central é obrigatória para gerar o relatório.");
        }

        //Guarda o dia e a hora em que o relatório foi gerado
        this.dataGeracao = LocalDateTime.now();

        //Esses métodos precisam existir na CentralResgate
        List<Missao> missoes = central.getMissoes();
        this.totalRobos = central.getRobos().size();
        this.totalMissoes = missoes.size();
        this.missoesPendentes = central.getMissoesPendentes().size();

        //stream() pega a lista de missões e passa uma por uma pelos passos abaixo.
        //filter(...) fica só com as missões em que a lambda dá true (aqui, as concluídas).
        //count() conta quantas sobraram. O (int) converte o resultado de long para int.
        this.missoesConcluidas = (int) missoes.stream()
            .filter(m -> m.getStatus() == StatusMissao.CONCLUIDA)
            .count();

        //Mesma ideia, agora contando as canceladas
        this.missoesCanceladas = (int) missoes.stream()
            .filter(m -> m.getStatus() == StatusMissao.CANCELADA)
            .count();
    }

    //Metodos Getters
    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public int getTotalRobos() {
        return totalRobos;
    }

    public int getTotalMissoes() {
        return totalMissoes;
    }

    public int getMissoesConcluidas() {
        return missoesConcluidas;
    }

    public int getMissoesPendentes() {
        return missoesPendentes;
    }

    //Metodo que monta o texto do relatório e devolve como String
    public String gerarResumo() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return "===RELATÓRIO GERAL===\n"
                + "Gerado em: " + dataGeracao.format(formato) + "\n"
                + "Total de robôs: " + totalRobos + "\n"
                + "Total de missões: " + totalMissoes + "\n"
                + "Missões concluídas: " + missoesConcluidas + "\n"
                + "Missões pendentes: " + missoesPendentes + "\n"
                + "Missões canceladas: " + missoesCanceladas;
    }

    //Metodo que mostra o relatório no console
    public void exibir() {
        System.out.println(gerarResumo());
    }

    //Metodo que salva o relatório em um arquivo de texto.
    //O try-with-resources (o parêntese depois do try) fecha o arquivo sozinho no final, mesmo se der erro.
    //Se der erro ao gravar, lança PersistenciaException.
    public void salvarEmArquivo(String caminho) throws PersistenciaException {
        try (FileWriter escritor = new FileWriter(caminho)) {
            escritor.write(gerarResumo());
        } catch (IOException e) {
            throw new PersistenciaException("ERRO - Não foi possível salvar o relatório em " + caminho + ".");
        }
    }
}