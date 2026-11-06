package app;
//Imports
import java.util.Scanner;
import enums.NivelRisco;
import enums.PrioridadeMissao;
import enums.TipoMissao;
import enums.TipoTerreno;
import excecoes.DadosInvalidosException;
import excecoes.MissaoInvalidaException;
import excecoes.PersistenciaException;
import excecoes.RoboIndisponivelException;
import modelo.*;
import persistencia.*;

//Classe principal do sistema. Mostra o menu no console e chama os métodos da CentralResgate.
//Lembrete: A Main não guarda nenhuma regra de negócio, ela só lê o que o usuário digita e repassa para a central.
public class Main {

    //Caminho do arquivo onde os dados do sistema são salvos e carregados.
    private static final String ARQUIVO_DADOS = "dados/central.dat";
    //Caminho do arquivo onde o relatório é salvo.
    private static final String ARQUIVO_RELATORIO = "dados/relatorio.txt";

    //Scanner único para o programa inteiro, assim não abrimos vários leitores do teclado.
    private static final Scanner teclado = new Scanner(System.in);
    private static CentralResgate central;

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   SISTEMA DE COORDENAÇÃO DE ROBÔS DE RESGATE");
        System.out.println("==============================================");

        iniciarCentral();

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");

            //Lembrete: Todas as exceções personalizadas são tratadas aqui, então um erro em uma opção não fecha o programa.
            try {
                switch (opcao) {
                    case 1:
                        cadastrarArea();
                        break;
                    case 2:
                        cadastrarRobo();
                        break;
                    case 3:
                        cadastrarMissao();
                        break;
                    case 4:
                        central.listarAreas();
                        break;
                    case 5:
                        central.listarRobos();
                        break;
                    case 6:
                        central.listarMissoes();
                        break;
                    case 7:
                        central.listarPendentes();
                        break;
                    case 8:
                        atribuirRobo();
                        break;
                    case 9:
                        central.iniciarMissao(lerInteiro("ID da missão que será iniciada: "));
                        System.out.println("Missão iniciada com sucesso.");
                        break;
                    case 10:
                        central.concluirMissao(lerInteiro("ID da missão que será concluída: "));
                        System.out.println("Missão concluída com sucesso.");
                        break;
                    case 11:
                        central.cancelarMissao(lerInteiro("ID da missão que será cancelada: "));
                        System.out.println("Missão cancelada com sucesso.");
                        break;
                    case 12:
                        central.processarFilaPendentes();
                        break;
                    case 13:
                        enviarMensagem();
                        break;
                    case 14:
                        atualizarRiscoArea();
                        break;
                    case 15:
                        gerarRelatorio();
                        break;
                    case 16:
                        Persistencia.salvar(central, ARQUIVO_DADOS);
                        System.out.println("Dados salvos em " + ARQUIVO_DADOS);
                        break;
                    case 17:
                        central = Persistencia.carregar(ARQUIVO_DADOS);
                        System.out.println("Dados carregados de " + ARQUIVO_DADOS);
                        break;
                    case 0:
                        System.out.println("Encerrando o sistema...");
                        break;
                    default:
                        System.out.println("ERRO - Opção inválida. Digite um número do menu.");
                }
            } catch (DadosInvalidosException e) {
                System.out.println(e.getMessage());
            } catch (RoboIndisponivelException e) {
                System.out.println(e.getMessage());
            } catch (MissaoInvalidaException e) {
                System.out.println(e.getMessage());
            } catch (PersistenciaException e) {
                System.out.println(e.getMessage());
            }
        } while (opcao != 0);

        teclado.close();
    }

    //Tenta abrir os dados salvos. Se não existir arquivo salvo, começa com os dados de exemplo da apresentação.
    private static void iniciarCentral() {
        try {
            central = Persistencia.carregar(ARQUIVO_DADOS);
            System.out.println("Dados anteriores carregados de " + ARQUIVO_DADOS);
        } catch (PersistenciaException e) {
            System.out.println("Nenhum dado salvo encontrado. Carregando dados de exemplo...");
            central = new CentralResgate();
            carregarDadosExemplo();
        }
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("----------------- MENU -----------------");
        System.out.println(" CADASTROS");
        System.out.println("  1 - Cadastrar área de operação");
        System.out.println("  2 - Cadastrar robô");
        System.out.println("  3 - Cadastrar missão");
        System.out.println(" CONSULTAS");
        System.out.println("  4 - Listar áreas");
        System.out.println("  5 - Listar robôs");
        System.out.println("  6 - Listar missões");
        System.out.println("  7 - Listar fila de missões pendentes");
        System.out.println(" OPERAÇÕES");
        System.out.println("  8 - Atribuir robô a uma missão");
        System.out.println("  9 - Iniciar missão");
        System.out.println(" 10 - Concluir missão");
        System.out.println(" 11 - Cancelar missão");
        System.out.println(" 12 - Processar fila de pendentes");
        System.out.println(" 13 - Enviar mensagem para um robô");
        System.out.println(" 14 - Atualizar nível de risco de uma área");
        System.out.println(" RELATÓRIO E ARQUIVOS");
        System.out.println(" 15 - Gerar relatório");
        System.out.println(" 16 - Salvar dados");
        System.out.println(" 17 - Carregar dados");
        System.out.println("  0 - Sair");
        System.out.println("----------------------------------------");
    }

    //---------------------- Ações do menu ----------------------

    private static void cadastrarArea() throws DadosInvalidosException {
        int id = lerInteiro("ID da área: ");
        String nome = lerTexto("Nome da área: ");
        String localizacao = lerTexto("Localização: ");
        TipoTerreno terreno = escolherOpcao("Tipo de terreno", TipoTerreno.values());
        NivelRisco risco = escolherOpcao("Nível de risco", NivelRisco.values());
        String descricao = lerTexto("Descrição (opcional): ");

        //Se algum dado for inválido, o próprio construtor da AreaOperacao lança DadosInvalidosException.
        central.cadastrarArea(new AreaOperacao(id, nome, localizacao, terreno, risco, descricao));
        System.out.println("Área cadastrada com sucesso.");
    }

    private static void cadastrarRobo() throws DadosInvalidosException {
        System.out.println("Tipo de robô:");
        System.out.println("1 - Drone");
        System.out.println("2 - Robô Terrestre");
        int tipo = lerInteiro("Escolha: ");
        if (tipo != 1 && tipo != 2) {
            throw new DadosInvalidosException("ERRO - Tipo de robô inválido. Escolha 1 ou 2.");
        }

        int id = lerInteiro("ID do robô: ");
        String nome = lerTexto("Nome do robô: ");
        double bateria = lerDecimal("Nível de bateria (0 a 100): ");

        //Lembrete: A variável é do tipo Robo (classe abstrata), mas o objeto criado é um Drone ou um RoboTerrestre (polimorfismo).
        Robo robo;
        if (tipo == 1) {
            robo = new Drone(id, nome, bateria);
        } else {
            robo = new RoboTerrestre(id, nome, bateria);
        }
        central.cadastrarRobo(robo);
        System.out.println("Robô cadastrado com sucesso.");
    }

    private static void cadastrarMissao() throws DadosInvalidosException {
        int id = lerInteiro("ID da missão: ");
        String descricao = lerTexto("Descrição da missão: ");
        TipoMissao tipo = escolherOpcao("Tipo de missão", TipoMissao.values());
        PrioridadeMissao prioridade = escolherOpcao("Prioridade", PrioridadeMissao.values());
        int idArea = lerInteiro("ID da área onde a missão acontece: ");
        AreaOperacao area = central.buscarArea(idArea);

        central.cadastrarMissao(new Missao(id, descricao, tipo, prioridade, area));
        System.out.println("Missão cadastrada com sucesso. Ela entrou na fila de pendentes.");
    }

    private static void atribuirRobo() throws RoboIndisponivelException, MissaoInvalidaException, DadosInvalidosException {
        int idMissao = lerInteiro("ID da missão: ");
        int idRobo = lerInteiro("ID do robô: ");
        central.atribuirRobo(idMissao, idRobo);
        System.out.println("Robô atribuído à missão com sucesso.");
    }

    private static void enviarMensagem() throws DadosInvalidosException {
        int idRobo = lerInteiro("ID do robô: ");
        String mensagem = lerTexto("Mensagem: ");
        central.enviarMensagem(idRobo, mensagem);
    }

    private static void atualizarRiscoArea() throws DadosInvalidosException {
        int idArea = lerInteiro("ID da área: ");
        AreaOperacao area = central.buscarArea(idArea);
        NivelRisco novoRisco = escolherOpcao("Novo nível de risco", NivelRisco.values());
        area.atualizarNivelRisco(novoRisco);
        System.out.println("Nível de risco atualizado para " + novoRisco + ".");
    }

    private static void gerarRelatorio() throws PersistenciaException {
        Relatorio relatorio = new Relatorio(central);
        relatorio.exibir();
        String resposta = lerTexto("Deseja salvar o relatório em arquivo? (S/N): ");
        if (resposta.equalsIgnoreCase("S")) {
            relatorio.salvarEmArquivo(ARQUIVO_RELATORIO);
            System.out.println("Relatório salvo em " + ARQUIVO_RELATORIO);
        }
    }

    //---------------------- Dados de exemplo ----------------------

    //Dados prontos para a apresentação, assim não precisamos cadastrar tudo na hora.
    //Cenário: enchentes no Rio Grande do Sul.
    private static void carregarDadosExemplo() {
        try {
            AreaOperacao sarandi = new AreaOperacao(1, "Bairro Sarandi", "Porto Alegre - RS", TipoTerreno.ALAGADO, NivelRisco.ALTO,
                    "Ruas alagadas com moradores ilhados em casas.");
            AreaOperacao centro = new AreaOperacao(2, "Centro Histórico", "Porto Alegre - RS", TipoTerreno.URBANO, NivelRisco.MEDIO,
                    "Prédios com acesso parcial e falta de energia.");
            AreaOperacao serra = new AreaOperacao(3, "Encosta da Serra", "Gramado - RS", TipoTerreno.IRREGULAR, NivelRisco.ALTO,
                    "Deslizamento de terra bloqueou a estrada.");
            central.cadastrarArea(sarandi);
            central.cadastrarArea(centro);
            central.cadastrarArea(serra);

            central.cadastrarRobo(new Drone(1, "Gavião-01", 95));
            central.cadastrarRobo(new Drone(2, "Coruja-02", 40));
            central.cadastrarRobo(new RoboTerrestre(3, "Tatu-03", 80));
            central.cadastrarRobo(new RoboTerrestre(4, "Jabuti-04", 15));

            central.cadastrarMissao(new Missao(1, "Sobrevoar o bairro e mapear as ruas alagadas",
                    TipoMissao.RECONHECIMENTO, PrioridadeMissao.MEDIA, sarandi));
            central.cadastrarMissao(new Missao(2, "Localizar moradores ilhados nos telhados",
                    TipoMissao.LOCALIZACAO_PESSOAS, PrioridadeMissao.ALTA, sarandi));
            central.cadastrarMissao(new Missao(3, "Levar água e remédios ao abrigo do centro",
                    TipoMissao.TRANSPORTE_SUPRIMENTOS, PrioridadeMissao.ALTA, centro));
            central.cadastrarMissao(new Missao(4, "Procurar pessoas soterradas na encosta",
                    TipoMissao.LOCALIZACAO_PESSOAS, PrioridadeMissao.ALTA, serra));

            System.out.println("Dados de exemplo carregados: 3 áreas, 4 robôs e 4 missões.");
        } catch (DadosInvalidosException e) {
            System.out.println("ERRO ao carregar dados de exemplo: " + e.getMessage());
        }
    }

    //---------------------- Leitura de entradas ----------------------
    //Lembrete: Todas as leituras são feitas com nextLine() e depois convertidas, assim uma letra digitada no lugar de um número não trava o programa.

    //Lê um número inteiro. Se o usuário digitar algo que não é número, mostra o erro e pede de novo.
    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = teclado.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("ERRO - \"" + entrada + "\" não é um número inteiro válido. Tente novamente.");
            }
        }
    }

    //Lê um número decimal. Aceita vírgula ou ponto (ex: 75,5 ou 75.5).
    private static double lerDecimal(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = teclado.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("ERRO - \"" + entrada + "\" não é um número válido. Tente novamente.");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return teclado.nextLine().trim();
    }

    //Mostra as opções de qualquer enumeração numeradas e devolve a escolhida.
    //Lembrete: O <T> deixa o mesmo método funcionar para TipoTerreno, NivelRisco, TipoMissao e PrioridadeMissao.
    private static <T> T escolherOpcao(String titulo, T[] opcoes) {
        System.out.println(titulo + ":");
        for (int i = 0; i < opcoes.length; i++) {
            System.out.println((i + 1) + " - " + opcoes[i]);
        }
        while (true) {
            int escolha = lerInteiro("Escolha: ");
            if (escolha >= 1 && escolha <= opcoes.length) {
                return opcoes[escolha - 1];
            }
            System.out.println("ERRO - Opção fora da lista. Digite um número de 1 a " + opcoes.length + ".");
        }
    }
}
