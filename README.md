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
- **Drone:** reconhecimento e localização de pessoas, em qualquer tipo de terreno.
- **Robô terrestre:** localização de pessoas e transporte de suprimentos, apenas em áreas do mesmo tipo de terreno que ele atua.

## Consumo de bateria
Cada missão tem um custo base (30 reconhecimento, 40 localização de pessoas, 50 transporte) somado a um adicional pela prioridade (0 baixa, 5 média, 10 alta). O robô só recebe a missão se tiver bateria suficiente.

## Funcionalidade inovadora
??? (Precisamos Terminar)

## Como usar o sistema
??? (Precisa da Main do João pronta)