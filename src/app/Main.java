package app;
//Imports
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import enums.NivelRisco;
import enums.PrioridadeMissao;
import enums.StatusMissao;
import enums.StatusRobo;
import enums.TipoMissao;
import enums.TipoTerreno;
import excecoes.DadosInvalidosException;
import excecoes.MissaoInvalidaException;
import excecoes.PersistenciaException;
import excecoes.RoboIndisponivelException;
import modelo.AreaOperacao;
import modelo.CentralResgate;
import modelo.Drone;
import modelo.Missao;
import modelo.Relatorio;
import modelo.Robo;
import modelo.RoboTerrestre;
import persistencia.Persistencia;

//Classe principal do sistema. Mostra o menu no console e chama os métodos da CentralResgate.
//Lembrete: A Main não guarda as regras de negócio, ela lê o que o usuário digita, repassa para a central e mostra o resultado.
public class Main {

    //Nome da central criada quando não existe nenhum dado salvo.
    private static final String NOME_CENTRAL = "Central de Resgate RS";
    //Caminho do arquivo onde os dados do sistema são salvos e carregados.
    private static final String ARQUIVO_DADOS = "dados/central.dat";
    //Caminho do arquivo onde o relatório é salvo.
    private static final String ARQUIVO_RELATORIO = "dados/relatorio.txt";

    //Scanner único para o programa inteiro, assim não abrimos vários leitores do teclado.
    private static final Scanner teclado = new Scanner(System.in);
    private static CentralResgate central;

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("  SISTEMA DE COORDENAÇÃO DE ROBÔS DE RESGATE");
        System.out.println("==============================================");

        iniciarCentral();

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");
            System.out.println();

            //Lembrete: Todas as exceções personalizadas são tratadas aqui, então um erro em uma opção não fecha o programa.
            //O "|" junta as quatro exceções no mesmo catch, porque o tratamento é o mesmo: mostrar a mensagem de erro.
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
                        listarAreas();
                        break;
                    case 5:
                        listarRobos();
                        break;
                    case 6:
                        central.listarRobosDisponiveis();
                        break;
                    case 7:
                        listarMissoes();
                        break;
                    case 8:
                        listarFilaPendentes();
                        break;
                    case 9:
                        central.exibirResumo();
                        break;
                    case 10:
                        atribuirRoboMissao();
                        break;
                    case 11:
                        finalizarMissao();
                        break;
                    case 12:
                        cancelarMissao();
                        break;
                    case 13:
                        atribuirMissoesUrgentes();
                        break;
                    case 14:
                        recarregarRobo();
                        break;
                    case 15:
                        comunicarComRobo();
                        break;
                    case 16:
                        atualizarRiscoArea();
                        break;
                    case 17:
                        gerarRelatorio();
                        break;
                    case 18:
                        Persistencia.salvar(central, ARQUIVO_DADOS);
                        System.out.println("Dados salvos em " + ARQUIVO_DADOS + ".");
                        break;
                    case 19:
                        central = Persistencia.carregar(ARQUIVO_DADOS);
                        System.out.println("Dados carregados de " + ARQUIVO_DADOS + ".");
                        break;
                    case 0:
                        sair();
                        break;
                    default:
                        System.out.println("ERRO - Opção inválida. Digite um número do menu.");
                }
            } catch (DadosInvalidosException | RoboIndisponivelException | MissaoInvalidaException | PersistenciaException e) {
                System.out.println(e.getMessage());
            }
        } while (opcao != 0);

        teclado.close();
    }

    //Se existir um arquivo salvo, abre ele. Se não existir (ou não der para ler), começa com os dados de exemplo.
    private static void iniciarCentral() {
        if (Persistencia.existeArquivo(ARQUIVO_DADOS)) {
            try {
                central = Persistencia.carregar(ARQUIVO_DADOS);
                System.out.println("Dados anteriores carregados de " + ARQUIVO_DADOS + ".");
                return;
            } catch (PersistenciaException e) {
                System.out.println(e.getMessage());
                System.out.println("Os dados de exemplo serão usados no lugar.");
            }
        } else {
            System.out.println("Nenhum dado salvo encontrado. Carregando dados de exemplo...");
        }
        central = new CentralResgate(NOME_CENTRAL);
        carregarDadosExemplo();
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
        System.out.println("  6 - Listar robôs disponíveis");
        System.out.println("  7 - Listar missões");
        System.out.println("  8 - Listar fila de missões pendentes");
        System.out.println("  9 - Resumo da central");
        System.out.println(" OPERAÇÕES");
        System.out.println(" 10 - Atribuir robô a uma missão (inicia a missão)");
        System.out.println(" 11 - Finalizar missão");
        System.out.println(" 12 - Cancelar missão");
        System.out.println(" 13 - Atribuir missões urgentes automaticamente");
        System.out.println(" 14 - Recarregar bateria de um robô");
        System.out.println(" 15 - Comunicação com um robô");
        System.out.println(" 16 - Atualizar nível de risco de uma área");
        System.out.println(" RELATÓRIO E ARQUIVOS");
        System.out.println(" 17 - Gerar relatório");
        System.out.println(" 18 - Salvar dados");
        System.out.println(" 19 - Carregar dados");
        System.out.println("  0 - Sair");
        System.out.println("----------------------------------------");
    }

    //---------------------- Cadastros ----------------------

    private static void cadastrarArea() throws DadosInvalidosException {
        int id = lerInteiro("ID da área: ");
        String nome = lerTexto("Nome da área: ");
        String localizacao = lerTexto("Localização: ");
        TipoTerreno terreno = escolherOpcao("Tipo de terreno", TipoTerreno.values());
        NivelRisco risco = escolherOpcao("Nível de risco", NivelRisco.values());
        String descricao = lerTexto("Descrição (opcional): ");

        //Se algum dado for inválido, o próprio construtor da AreaOperacao lança DadosInvalidosException.
        AreaOperacao area = new AreaOperacao(id, nome, localizacao, terreno, risco, descricao);

        //A central devolve false quando já existe uma área com esse ID no HashMap.
        if (!central.cadastrarArea(id, area)) {
            throw new DadosInvalidosException("ERRO - Já existe uma área cadastrada com o ID " + id + ".");
        }
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
        if (id <= 0) {
            throw new DadosInvalidosException("ERRO - O ID do robô deve ser maior que zero.");
        }
        if (procurarRobo(id) != null) {
            throw new DadosInvalidosException("ERRO - Já existe um robô cadastrado com o ID " + id + ".");
        }

        String nome = lerTexto("Nome do robô: ");
        if (nome.isEmpty()) {
            throw new DadosInvalidosException("ERRO - O nome do robô é obrigatório e não pode ser vazio.");
        }

        //Lembrete: A variável é do tipo Robo (classe abstrata), mas o objeto criado é um Drone ou um RoboTerrestre (polimorfismo).
        //Todo robô novo começa DISPONIVEL e com 100% de bateria.
        Robo robo;
        if (tipo == 1) {
            boolean imagem = lerSimNao("O drone captura imagens? (S/N): ");
            int altitudeMax = lerInteiro("Altitude máxima (metros): ");
            if (altitudeMax <= 0) {
                throw new DadosInvalidosException("ERRO - A altitude máxima deve ser maior que zero.");
            }
            robo = new Drone(id, nome, StatusRobo.DISPONIVEL, imagem, altitudeMax);
        } else {
            int velocidadeMax = lerInteiro("Velocidade máxima (km/h): ");
            if (velocidadeMax <= 0) {
                throw new DadosInvalidosException("ERRO - A velocidade máxima deve ser maior que zero.");
            }
            TipoTerreno terreno = escolherOpcao("Terreno em que o robô atua", TipoTerreno.values());
            double cargaMax = lerDecimal("Carga máxima (kg): ");
            if (cargaMax <= 0) {
                throw new DadosInvalidosException("ERRO - A carga máxima deve ser maior que zero.");
            }
            robo = new RoboTerrestre(id, nome, StatusRobo.DISPONIVEL, velocidadeMax, terreno, cargaMax);
        }

        central.cadastrarRobo(robo);
        System.out.println("Robô cadastrado com sucesso, com 100% de bateria.");
    }

    private static void cadastrarMissao() throws DadosInvalidosException {
        int id = lerInteiro("ID da missão: ");
        if (procurarMissao(id) != null) {
            throw new DadosInvalidosException("ERRO - Já existe uma missão cadastrada com o ID " + id + ".");
        }
        String descricao = lerTexto("Descrição da missão: ");
        TipoMissao tipo = escolherOpcao("Tipo de missão", TipoMissao.values());
        PrioridadeMissao prioridade = escolherOpcao("Prioridade", PrioridadeMissao.values());
        AreaOperacao area = buscarArea(lerInteiro("ID da área onde a missão acontece: "));

        //Se algum dado for inválido, o próprio construtor da Missao lança DadosInvalidosException.
        central.registrarMissao(new Missao(id, descricao, tipo, prioridade, area));
        System.out.println("Missão cadastrada com sucesso. Ela entrou na fila de pendentes.");
    }

    //---------------------- Consultas ----------------------

    private static void listarAreas() {
        if (central.getAreasOperacao().isEmpty()) {
            System.out.println("Nenhuma área cadastrada.");
            return;
        }
        System.out.println("Áreas de Operação:");
        for (AreaOperacao area : central.getAreasOperacao().values()) {
            System.out.println();
            area.exibirInformacoes();
        }
    }

    private static void listarRobos() {
        if (central.getRobos().isEmpty()) {
            System.out.println("Nenhum robô cadastrado.");
            return;
        }
        System.out.println("Robôs:");
        for (Robo robo : central.getRobos()) {
            //Lembrete: O exibirResumo() chamado é o da subclasse (Drone ou RoboTerrestre), cada uma mostra seus próprios atributos.
            String tipo = (robo instanceof Drone) ? "Drone" : "Robô Terrestre";
            System.out.println();
            System.out.println("[" + tipo + "]");
            System.out.println(robo.exibirResumo());
        }
    }

    private static void listarMissoes() {
        if (central.getMissoes().isEmpty()) {
            System.out.println("Nenhuma missão cadastrada.");
            return;
        }
        System.out.println("Missões:");
        //As missões ficam em um HashSet, que não guarda ordem. O sorted(...) organiza pelo ID antes de mostrar.
        central.getMissoes().stream()
                .sorted(Comparator.comparingInt(Missao::getIdMissao))
                .forEach(missao -> {
                    System.out.println();
                    missao.exibirResumo();
                });
    }

    //Mostra só as missões que ainda esperam um robô, na ordem em que entraram na fila.
    private static void listarFilaPendentes() {
        System.out.println("Fila de missões pendentes (na ordem de chegada):");
        boolean temPendente = false;
        for (Missao missao : central.getMissoesPendentes()) {
            if (missao.getStatus() == StatusMissao.PENDENTE) {
                System.out.println("#" + missao.getIdMissao() + " - " + missao.getTipo() + " - Prioridade " + missao.getPrioridade()
                        + " - Área: " + missao.getArea().getNomeArea());
                temPendente = true;
            }
        }
        if (!temPendente) {
            System.out.println("Nenhuma missão pendente.");
        }
    }

    //---------------------- Operações ----------------------

    //Liga um robô a uma missão. A central já inicia a missão na mesma hora (status EM_ANDAMENTO).
    private static void atribuirRoboMissao() throws DadosInvalidosException, RoboIndisponivelException, MissaoInvalidaException {
        Missao missao = buscarMissao(lerInteiro("ID da missão: "));
        Robo robo = buscarRobo(lerInteiro("ID do robô: "));

        if (central.atribuirRoboMissao(robo, missao)) {
            System.out.println("Robô " + robo.getNome() + " atribuído à missão #" + missao.getIdMissao() + ". Missão iniciada (EM_ANDAMENTO).");
            return;
        }

        //A central só devolve false, sem dizer o motivo. Aqui descobrimos qual regra impediu, para mostrar a mensagem certa.
        explicarFalhaAtribuicao(robo, missao);
    }

    private static void explicarFalhaAtribuicao(Robo robo, Missao missao) throws RoboIndisponivelException, MissaoInvalidaException {
        if (missao.getStatus() != StatusMissao.PENDENTE) {
            throw new MissaoInvalidaException("ERRO - A missão #" + missao.getIdMissao() + " não está pendente (status atual: " + missao.getStatus() + ").");
        }
        if (robo.getStatus() != StatusRobo.DISPONIVEL) {
            throw new RoboIndisponivelException("ERRO - O robô " + robo.getNome() + " não está disponível (status atual: " + robo.getStatus() + ").");
        }
        if (!robo.checarTipoMissao(missao)) {
            throw new MissaoInvalidaException("ERRO - O robô " + robo.getNome() + " não realiza missões do tipo " + missao.getTipo() + ".");
        }
        if (!robo.verificarCompatibilidadeArea(missao.getArea())) {
            throw new MissaoInvalidaException("ERRO - O robô " + robo.getNome() + " não atua no terreno da área " + missao.getArea().getNomeArea()
                    + " (" + missao.getArea().getTipoTerreno() + ").");
        }
        if (!robo.checarBateriaMinima(missao)) {
            throw new RoboIndisponivelException("ERRO - O robô " + robo.getNome() + " não tem bateria suficiente para essa missão (bateria atual: "
                    + robo.getNivelBateria() + "%).");
        }
        throw new MissaoInvalidaException("ERRO - Não foi possível atribuir o robô " + robo.getNome() + " à missão #" + missao.getIdMissao() + ".");
    }

    private static void finalizarMissao() throws DadosInvalidosException, MissaoInvalidaException {
        Missao missao = buscarMissao(lerInteiro("ID da missão que será finalizada: "));
        if (missao.getStatus() != StatusMissao.EM_ANDAMENTO) {
            throw new MissaoInvalidaException("ERRO - Só é possível finalizar uma missão que está em andamento. A missão #"
                    + missao.getIdMissao() + " está " + missao.getStatus() + ".");
        }
        String resultado = lerTexto("Resultado da missão: ");
        Robo robo = missao.getRoboResponsavel();

        //A central conclui a missão e o robô gasta a bateria da missão e volta a ficar disponível.
        central.finalizarMissao(missao, resultado);
        if (missao.getStatus() != StatusMissao.CONCLUIDA) {
            throw new MissaoInvalidaException("ERRO - A central não conseguiu finalizar a missão #" + missao.getIdMissao() + ".");
        }

        //A missão terminou, então sai da fila de pendentes.
        central.getMissoesPendentes().remove(missao);
        System.out.println("Missão #" + missao.getIdMissao() + " concluída.");
        System.out.println("O robô " + robo.getNome() + " ficou com " + robo.getNivelBateria() + "% de bateria (" + robo.getStatus() + ").");
    }

    private static void cancelarMissao() throws DadosInvalidosException, MissaoInvalidaException {
        Missao missao = buscarMissao(lerInteiro("ID da missão que será cancelada: "));
        StatusMissao statusAnterior = missao.getStatus();

        //Se a missão já foi concluída ou cancelada, o próprio cancelar() da Missao lança MissaoInvalidaException.
        missao.cancelar();
        central.getMissoesPendentes().remove(missao);
        System.out.println("Missão #" + missao.getIdMissao() + " cancelada.");

        //Se a missão estava em andamento, o robô que estava nela fica livre para outra missão.
        Robo robo = missao.getRoboResponsavel();
        if (statusAnterior == StatusMissao.EM_ANDAMENTO && robo != null && robo.getStatus() == StatusRobo.EM_MISSAO) {
            robo.setStatus(StatusRobo.DISPONIVEL);
            System.out.println("O robô " + robo.getNome() + " foi liberado e voltou a ficar DISPONIVEL.");
        }
    }

    //A central percorre a fila e manda o primeiro robô compatível para cada missão de prioridade ALTA.
    private static void atribuirMissoesUrgentes() {
        //Guarda antes quais missões urgentes estavam esperando, para depois mostrar o que aconteceu com cada uma.
        List<Missao> urgentes = new ArrayList<>();
        for (Missao missao : central.getMissoesPendentes()) {
            if (missao.getPrioridade() == PrioridadeMissao.ALTA && missao.getStatus() == StatusMissao.PENDENTE) {
                urgentes.add(missao);
            }
        }
        if (urgentes.isEmpty()) {
            System.out.println("Nenhuma missão urgente (prioridade ALTA) está pendente.");
            return;
        }

        central.atribuirMissoesUrgentes();

        System.out.println("Missões urgentes:");
        for (Missao missao : urgentes) {
            if (missao.getStatus() == StatusMissao.EM_ANDAMENTO) {
                System.out.println("#" + missao.getIdMissao() + " - atribuída ao robô " + missao.getRoboResponsavel().getNome() + " e iniciada.");
            } else {
                System.out.println("#" + missao.getIdMissao() + " - continua pendente: nenhum robô disponível e compatível.");
            }
        }
    }

    private static void recarregarRobo() throws DadosInvalidosException {
        Robo robo = buscarRobo(lerInteiro("ID do robô: "));
        System.out.println(central.recarregarRobo(robo) + ": " + robo.getNome() + " está com " + robo.getNivelBateria() + "%.");
    }

    //Usa os dois métodos da interface Comunicavel: o robô recebe mensagens da central e envia mensagens para ela.
    private static void comunicarComRobo() throws DadosInvalidosException {
        Robo robo = buscarRobo(lerInteiro("ID do robô: "));
        System.out.println("1 - Central envia mensagem para o robô");
        System.out.println("2 - Robô envia mensagem para a central");
        int sentido = lerInteiro("Escolha: ");
        if (sentido != 1 && sentido != 2) {
            throw new DadosInvalidosException("ERRO - Opção inválida. Escolha 1 ou 2.");
        }
        String mensagem = lerTexto("Mensagem: ");
        if (mensagem.isEmpty()) {
            throw new DadosInvalidosException("ERRO - A mensagem não pode ser vazia.");
        }

        if (sentido == 1) {
            System.out.print("[Central -> " + robo.getNome() + "] ");
            central.receberMensagemRobo(robo, mensagem);
        } else {
            System.out.print("[" + robo.getNome() + " -> Central] ");
            central.enviarMensagemRobo(robo, mensagem);
        }
    }

    private static void atualizarRiscoArea() throws DadosInvalidosException {
        AreaOperacao area = buscarArea(lerInteiro("ID da área: "));
        NivelRisco novoRisco = escolherOpcao("Novo nível de risco", NivelRisco.values());
        area.atualizarNivelRisco(novoRisco);
        System.out.println("Nível de risco da área " + area.getNomeArea() + " atualizado para " + novoRisco + ".");
    }

    private static void gerarRelatorio() throws PersistenciaException {
        Relatorio relatorio = central.gerarRelatorioGeral();
        relatorio.exibir();
        if (lerSimNao("Deseja salvar o relatório em arquivo? (S/N): ")) {
            relatorio.salvarEmArquivo(ARQUIVO_RELATORIO);
            System.out.println("Relatório salvo em " + ARQUIVO_RELATORIO + ".");
        }
    }

    private static void sair() {
        if (lerSimNao("Deseja salvar os dados antes de sair? (S/N): ")) {
            try {
                Persistencia.salvar(central, ARQUIVO_DADOS);
                System.out.println("Dados salvos em " + ARQUIVO_DADOS + ".");
            } catch (PersistenciaException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Encerrando o sistema...");
    }

    //---------------------- Busca por ID ----------------------
    //Lembrete: A CentralResgate ainda não tem métodos de busca por ID, então a Main procura nas coleções que a central devolve.

    //Devolve o robô com esse ID, ou null se não existir.
    private static Robo procurarRobo(int id) {
        for (Robo robo : central.getRobos()) {
            if (robo.getId() == id) {
                return robo;
            }
        }
        return null;
    }

    //Devolve a missão com esse ID, ou null se não existir.
    private static Missao procurarMissao(int id) {
        for (Missao missao : central.getMissoes()) {
            if (missao.getIdMissao() == id) {
                return missao;
            }
        }
        return null;
    }

    //Mesma busca, mas se não encontrar lança DadosInvalidosException.
    private static Robo buscarRobo(int id) throws DadosInvalidosException {
        Robo robo = procurarRobo(id);
        if (robo == null) {
            throw new DadosInvalidosException("ERRO - Nenhum robô cadastrado com o ID " + id + ".");
        }
        return robo;
    }

    private static Missao buscarMissao(int id) throws DadosInvalidosException {
        Missao missao = procurarMissao(id);
        if (missao == null) {
            throw new DadosInvalidosException("ERRO - Nenhuma missão cadastrada com o ID " + id + ".");
        }
        return missao;
    }

    //As áreas ficam em um HashMap com o ID como chave, então dá para buscar direto pelo get(id).
    private static AreaOperacao buscarArea(int id) throws DadosInvalidosException {
        AreaOperacao area = central.getAreasOperacao().get(id);
        if (area == null) {
            throw new DadosInvalidosException("ERRO - Nenhuma área cadastrada com o ID " + id + ".");
        }
        return area;
    }

    //---------------------- Dados de exemplo ----------------------

    //Dados prontos para a apresentação, assim não precisamos cadastrar tudo na hora.
    //Cenário: enchentes de maio de 2024 no Rio Grande do Sul.
    private static void carregarDadosExemplo() {
        try {
            AreaOperacao sarandi = new AreaOperacao(1, "Bairro Sarandi", "Porto Alegre - RS", TipoTerreno.ALAGADO, NivelRisco.ALTO,
                    "Ruas alagadas com moradores ilhados em casas.");
            AreaOperacao centro = new AreaOperacao(2, "Centro Histórico", "Porto Alegre - RS", TipoTerreno.URBANO, NivelRisco.MEDIO,
                    "Abrigo no ginásio, com acesso parcial e falta de energia.");
            AreaOperacao serra = new AreaOperacao(3, "Encosta da Serra", "Gramado - RS", TipoTerreno.IRREGULAR, NivelRisco.ALTO,
                    "Deslizamento de terra bloqueou a estrada.");
            central.cadastrarArea(sarandi.getIdArea(), sarandi);
            central.cadastrarArea(centro.getIdArea(), centro);
            central.cadastrarArea(serra.getIdArea(), serra);

            central.cadastrarRobo(new Drone(1, "Gavião-01", StatusRobo.DISPONIVEL, true, 120));
            central.cadastrarRobo(new Drone(2, "Coruja-02", StatusRobo.DISPONIVEL, false, 80));
            central.cadastrarRobo(new RoboTerrestre(3, "Tatu-03", StatusRobo.DISPONIVEL, 12, TipoTerreno.URBANO, 80));
            central.cadastrarRobo(new RoboTerrestre(4, "Capivara-04", StatusRobo.DISPONIVEL, 8, TipoTerreno.ALAGADO, 150));

            central.registrarMissao(new Missao(1, "Sobrevoar o bairro e mapear as ruas alagadas",
                    TipoMissao.RECONHECIMENTO, PrioridadeMissao.MEDIA, sarandi));
            central.registrarMissao(new Missao(2, "Localizar moradores ilhados nos telhados",
                    TipoMissao.LOCALIZACAO_PESSOAS, PrioridadeMissao.ALTA, sarandi));
            central.registrarMissao(new Missao(3, "Levar água potável e remédios ao abrigo",
                    TipoMissao.TRANSPORTE_SUPRIMENTOS, PrioridadeMissao.ALTA, centro));
            central.registrarMissao(new Missao(4, "Procurar pessoas soterradas na encosta",
                    TipoMissao.LOCALIZACAO_PESSOAS, PrioridadeMissao.ALTA, serra));
            central.registrarMissao(new Missao(5, "Levar coletes salva-vidas até os barcos",
                    TipoMissao.TRANSPORTE_SUPRIMENTOS, PrioridadeMissao.BAIXA, sarandi));

            System.out.println("Dados de exemplo carregados: 3 áreas, 4 robôs e 5 missões.");
        } catch (DadosInvalidosException e) {
            System.out.println("ERRO ao carregar dados de exemplo: " + e.getMessage());
        }
    }

    //---------------------- Leitura de entradas ----------------------
    //Lembrete: Todas as leituras são feitas com nextLine() e depois convertidas, assim uma letra digitada no lugar de um número não trava o programa.

    //Lê uma linha do teclado. Se a entrada acabar (ex: o console foi fechado), encerra o programa sem erro.
    private static String lerLinha() {
        if (!teclado.hasNextLine()) {
            System.out.println();
            System.out.println("Entrada encerrada. Saindo do sistema...");
            System.exit(0);
        }
        return teclado.nextLine().trim();
    }

    //Lê um número inteiro. Se o usuário digitar algo que não é número, mostra o erro e pede de novo.
    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = lerLinha();
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
            String entrada = lerLinha().replace(',', '.');
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("ERRO - \"" + entrada + "\" não é um número válido. Tente novamente.");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return lerLinha();
    }

    //Lê uma resposta S ou N. Qualquer outra coisa pede de novo.
    private static boolean lerSimNao(String mensagem) {
        while (true) {
            String resposta = lerTexto(mensagem);
            if (resposta.equalsIgnoreCase("S")) {
                return true;
            }
            if (resposta.equalsIgnoreCase("N")) {
                return false;
            }
            System.out.println("ERRO - Responda apenas S ou N.");
        }
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
