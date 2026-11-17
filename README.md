# Sistema de Coordenação de Robôs para Missões de Resgate
**Trabalho Final — Programação Orientada a Objetos | PUCRS — 2026/02**

> "Hope is being able to see that there is light despite all of the darkness." — Desmond Tutu
> "Esperança é ser capaz de enxergar a luz apesar de toda a escuridão." — Desmond Tutu

## Integrantes:
- *Anébes Caumo Lima* — 26106307
- *João Vitor Kluge Farias* — 26103549
- *Thiago Iãnez* — 26105070

## Sobre o projeto:
Este projeto foi proposto pela professora *Andrea Aparecida Konzen*, na disciplina de POO, a partir de uma reflexão que ultrapassa os limites da sala de aula e da própria programação.

Em maio de 2024, o Rio Grande do Sul enfrentou uma das maiores tragédias da sua história. As enchentes que atingiram nosso estado deixaram marcas profundas em inúmeras comunidades, famílias e vidas. Em meio à devastação, também testemunhamos algo extraordinário: a solidariedade humana. Pessoas de diferentes lugares se mobilizaram para resgatar desconhecidos, distribuir alimentos, transportar suprimentos e oferecer ajuda aqueles que haviam perdido quase tudo. Nesse cenário, a tecnologia também demonstrou seu enorme potencial como instrumento de apoio.

Foi a partir dessa realidade que surgiu a proposta deste trabalho: imaginar como os conhecimentos que estamos adquirindo hoje, ainda enquanto estudantes, poderiam ser utilizados para fazer a diferença diante de situações como essa. Mais do que desenvolver um sistema capaz de coordenar drones e robôs terrestres em missões de resgate, buscamos explorar como a tecnologia pode auxiliar na identificação de áreas de risco, na localização de pessoas e no transporte de suprimentos, contribuindo para tornar as operações de socorro mais eficientes e seguras.

Embora a inspiração desse projeto tenha surgido de uma tragédia vivida pelo nosso próprio estado, acreditamos que essa preocupação não deve se limitar somente à ele. Desastres naturais acontecem em diferentes partes do mundo, atingindo comunidades que, muitas vezes, precisam de toda a ajuda possível. Por isso, enxergamos este projeto não apenas como um TF (Trabalho Final), mas como uma oportunidade de refletir sobre a responsabilidade que acompanha o conhecimento e sobre as inúmeras possibilidades de colocá-lo a serviço da sociedade.

No fim, talvez o maior propósito deste trabalho não esteja apenas no sistema que desenvolvemos, nas funcionalidades implementadas ou nos conceitos de programação que aprendemos ao longo desse segundo semestre, mas naquilo que ele representa: a compreensão de que a tecnologia ganha um significado ainda maior quando utilizada em benefício de outras pessoas.

Porque, diante de qualquer adversidade, salvar uma vida, oferecer esperança e estender a mão a quem precisa sempre será a aplicação mais valiosa de qualquer conhecimento.

— Thiago Iãnez, 03/11/2026

## Estrutura do projeto
- `src/enums`: enumerações (status, níveis de risco, tipos de terreno e de missão)
- `src/modelo`: classes principais (Robo, Drone, RoboTerrestre, Missao, AreaOperacao, CentralResgate, Relatorio)
- `src/excecoes`: exceções personalizadas
- `src/persistencia`: salvar e carregar os dados em arquivo
- `src/app`: classe Main com o menu do console
- `dados/`: arquivos de dados de exemplo

## Regras de compatibilidade
Para um robô receber uma missão, a central confere quatro coisas, nesta ordem:

1. **A missão está PENDENTE.** Missão em andamento, concluída ou cancelada não recebe outro robô.
2. **O robô está DISPONIVEL.** Robô em missão, em manutenção ou sem bateria fica de fora.
3. **O robô faz aquele tipo de missão e atua no terreno da área:**

| Robô | Reconhecimento | Localização de pessoas | Transporte de suprimentos | Terreno |
|---|:---:|:---:|:---:|---|
| Drone | ✔ | ✔ | ✘ | qualquer um (ele voa) |
| Robô terrestre | ✘ | ✔ | ✔ | só o tipo de terreno em que ele atua (URBANO, ALAGADO ou IRREGULAR) |

4. **O robô tem bateria suficiente** para a missão inteira (ver abaixo).

Se alguma regra falhar, a missão continua pendente e o sistema mostra o motivo com uma mensagem `ERRO - ...`.

Além disso, cada robô e cada missão tem um **ID único**: a central compara o ID (e não o objeto) antes de cadastrar, então não existem dois robôs ou duas missões com o mesmo número.

## Consumo de bateria
A bateria é uma porcentagem e fica sempre entre **0 e 100**: o `setNivelBateria` recusa qualquer valor fora disso com `DadosInvalidosException`. Todo robô começa com 100%.

O gasto de uma missão é o **custo base do tipo de missão** somado a um **adicional pela prioridade** (missões urgentes exigem que o robô vá mais rápido, então gastam mais):

| Tipo de missão | Custo base | + Baixa (0) | + Média (5) | + Alta (10) |
|---|:---:|:---:|:---:|:---:|
| Reconhecimento | 30% | 30% | 35% | 40% |
| Localização de pessoas | 40% | 40% | 45% | 50% |
| Transporte de suprimentos | 50% | 50% | 55% | 60% |

- O robô só **recebe** a missão se a bateria atual for maior ou igual ao gasto dela.
- A bateria é **descontada quando a missão é finalizada**. Depois disso o robô volta a ficar DISPONIVEL.
- Se a bateria chegar a **0%**, o robô fica **SEM_BATERIA** e só volta a receber missões depois de ser recarregado (opção 14 do menu, que volta a bateria para 100%).

Exemplo: a Capivara-04 (100%) faz a missão 2, localização de pessoas com prioridade alta. Ela gasta 40 + 10 = 50% e termina com 50%.

## Funcionalidade inovadora
**Planejamento de bateria e atribuição automática das missões urgentes.**

Num resgate de verdade, mandar um robô que fica sem bateria no meio do caminho é perder o robô e a missão. Por isso a central calcula, antes de tudo, quanto cada robô vai gastar:

- **Cálculo da bateria necessária** (`Robo.calcularBateriaNecessaria`): cada subclasse informa o custo base das missões que realiza (`custoBase`, método abstrato), e a classe `Robo` soma o adicional da prioridade. Com isso, `checarBateriaMinima` e `executarMissao` ficam escritos uma vez só na classe mãe e funcionam para o Drone e para o Robô Terrestre (polimorfismo).
- **Consulta antes de mandar o robô** (opção **20** do menu): para uma missão escolhida, a central mostra, robô por robô, quanto ele gastaria e com quanto ficaria depois, ou o motivo de não poder ir (tipo de missão, terreno ou falta de bateria).
- **Missões urgentes automáticas** (opção **13** do menu): a central percorre a fila e, para cada missão de prioridade ALTA que ainda está PENDENTE, escolhe entre os robôs compatíveis **o que vai ficar com mais bateria depois da missão**. Assim sobra mais carga na equipe para as próximas emergências. Em caso de empate, vai o robô cadastrado primeiro. Missões que já começaram são ignoradas, e as que nenhum robô consegue assumir continuam pendentes.

Exemplo com os dados iniciais: a opção 20 na missão 4 (Encosta da Serra, terreno IRREGULAR) mostra que só os drones podem ir, gastando 50% cada. A opção 13 manda o Tatu-03 para a missão 3 e o Gavião-01 para a missão 4.

## Como usar o sistema

### Executando
O programa roda no console e precisa do Java 17 ou mais novo.

**No IntelliJ:** abra a pasta do projeto, clique com o botão direito em `src` → *Mark Directory as* → *Sources Root* (se ainda não estiver marcada) e execute a classe `app.Main`.

**Pelo terminal**, a partir da pasta do projeto:
```
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out app.Main
```
> O programa precisa ser executado a partir da pasta do projeto, porque é nela que fica a pasta `dados/`.

### Primeira execução
Se não existir nenhum arquivo salvo, o sistema começa com dados de exemplo inspirados nas enchentes de 2024 no RS:

| Áreas | Terreno | Risco |
|---|---|---|
| 1 - Bairro Sarandi (Porto Alegre) | ALAGADO | ALTO |
| 2 - Centro Histórico (Porto Alegre) | URBANO | MEDIO |
| 3 - Encosta da Serra (Gramado) | IRREGULAR | ALTO |

| Robôs | Tipo | Detalhe |
|---|---|---|
| 1 - Gavião-01 | Drone | captura imagens, 120 m de altitude |
| 2 - Coruja-02 | Drone | sem câmera, 80 m de altitude |
| 3 - Tatu-03 | Robô terrestre | terreno URBANO, 80 kg de carga |
| 4 - Capivara-04 | Robô terrestre | terreno ALAGADO, 150 kg de carga |

| Missões | Tipo | Prioridade | Área |
|---|---|---|---|
| 1 - Mapear as ruas alagadas | RECONHECIMENTO | MEDIA | 1 |
| 2 - Localizar moradores ilhados | LOCALIZACAO_PESSOAS | ALTA | 1 |
| 3 - Levar água e remédios ao abrigo | TRANSPORTE_SUPRIMENTOS | ALTA | 2 |
| 4 - Procurar pessoas soterradas | LOCALIZACAO_PESSOAS | ALTA | 3 |
| 5 - Levar coletes salva-vidas | TRANSPORTE_SUPRIMENTOS | BAIXA | 1 |

Todos os robôs começam disponíveis e com 100% de bateria.

### Menu
| Opção | O que faz |
|---|---|
| 1 a 3 | Cadastra uma área, um robô (drone ou terrestre) ou uma missão. Toda missão nova entra na fila como PENDENTE. |
| 4 a 9 | Lista áreas, robôs, robôs disponíveis, missões, a fila de pendentes e o resumo da central. |
| 10 | Atribui um robô a uma missão. Se o robô for compatível e tiver bateria, a missão já começa (EM_ANDAMENTO). |
| 11 | Finaliza uma missão em andamento e registra o resultado. O robô gasta a bateria da missão e volta a ficar disponível. |
| 12 | Cancela uma missão. Se ela estava em andamento, o robô é liberado. |
| 13 | Atribui automaticamente as missões de prioridade ALTA que estão pendentes, escolhendo o robô compatível que fica com mais bateria depois da missão. |
| 14 | Recarrega a bateria de um robô para 100%. |
| 15 | Troca mensagens entre a central e um robô (interface `Comunicavel`). |
| 16 | Atualiza o nível de risco de uma área. |
| 17 | Mostra o relatório geral e, se quiser, salva em `dados/relatorio.txt`. |
| 18 / 19 | Salva / carrega todos os dados em `dados/central.dat`. |
| 20 | Mostra quanto de bateria cada robô gastaria numa missão e com quanto ficaria depois (funcionalidade inovadora). |
| 0 | Sai do sistema (pergunta se quer salvar antes). |

### Exemplo rápido
1. `10` → missão `2`, robô `4`: a Capivara-04 começa a localizar os moradores ilhados.
2. `11` → missão `2`, resultado `12 moradores resgatados`: a missão é concluída e a Capivara-04 fica com 50% de bateria.
3. `13`: as missões urgentes 3 e 4 recebem os robôs Tatu-03 e Gavião-01.
4. `17`: o relatório mostra 5 missões, 1 concluída e 2 pendentes.

### Erros tratados
O sistema não fecha quando algo dá errado: ele mostra uma mensagem começando com `ERRO -` e volta para o menu. Alguns exemplos:
- letras no lugar de números, ou uma opção que não existe no menu;
- IDs repetidos, IDs menores ou iguais a zero e campos obrigatórios vazios;
- robô que não faz aquele tipo de missão, que não atua no terreno da área, que já está em missão ou que não tem bateria suficiente;
- finalizar uma missão que não começou, ou cancelar uma que já terminou;
- carregar os dados sem nenhum arquivo salvo.

### Dados salvos
Ao abrir, o sistema carrega `dados/central.dat` se ele existir. Para voltar aos dados de exemplo, apague esse arquivo.