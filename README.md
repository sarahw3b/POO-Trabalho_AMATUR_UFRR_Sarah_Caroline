# POO-Trabalho_AMATUR_UFRR_Sarah_Caroline
**Disciplina:** Programação Orientada a Objetos (Java).   
**Prof.:** Jean Bertrand.
**Tema:** Domando o Caos com Threads e Sincronização.

==============================================

**● OBJETIVO:** Simular o problema real de concorrência em venda de passagens na AMATUR e implementar técnicas de sincronização para resolver:

**1.** Race condition;

**2.** Seção crítica (synchronized);

**3.** Produtor/Consumidor com wait/notify;

**4.** Controle de fluxo com semáforos.

=============================================

Cada classe representa um elemento da simulação:

● Onibus → controla assentos e regras de concorrência.
● AgenteVenda → threads simulando vendedores.
● Cancelamento → produtor que libera assentos.
● Main → executa as partes I, II, III e IV.

==============================================

**● 1. PARTE I – Race Condition (Caos)**

Acontece que, sem sincronização, várias threads verificam o mesmo valor de assentos e vendem mais assentos do que existem, gerando overbooking.

**Trecho responsável:**

`public void reservarSemSincronizacao(...) {
    if (assentosDisponiveis > 0) {
        Thread.sleep(100);
        assentosDisponiveis--;   // NÃO PROTEGIDO → raça condition
    }
}`

**PRINT DO CONSOLE:**
Abaixo está um print realista do console da Parte I (sem sincronização), onde ocorre a race condition: múltiplas threads verificam assentos ao mesmo tempo e vendem mais do que existe.
<img width="380" height="296" alt="image" src="https://github.com/user-attachments/assets/bedec3a4-0b30-4016-b36d-2230f9574e07" />

*Deveria parar em 0, mas termina em –2, provando a condição de corrida.*

===============================================

**● 2. Como o synchronized resolveu a inconsistência de dados.**

Na Parte II, substituí o acesso inseguro ao assento por:

`synchronized (this) {
    if (assentosDisponiveis > 0) {
        Thread.sleep(100);
        assentosDisponiveis--;
    }
}`

**● Por que funciona?**
Porque o synchronized transforma a checagem + decremento em uma operação atômica:
- Somente UMA thread por vez entra na seção crítica.
- Outras threads ficam bloqueadas (não executam essa parte do código).
- Isso impede duas threads de verem o mesmo valor e tentarem vender a mesma vaga.

**Resultado:**
NÃO existe overbooking, assentos nunca ficam negativos, cada venda é processada de forma sequencial e consistente. Ou seja, o synchronized cria um mutex natural, garantindo consistência dos dados.

===============================================

**● 3. Como wait() e notify() economizam CPU evitando busy-waiting.**

Na Parte III, quando o ônibus está cheio, um agente executa:

`while (assentosDisponiveis <= 0) {
    wait();
}
`
Sem wait() o código ficaria assim:

`while (assentosDisponiveis <= 0) {
    // verifica de novo
}`

É chamado de busy-waiting a thread fica presa em um loop infinito gastando 100% de CPU, apenas verificando uma condição repetidamente, o que é extremamente ineficiente e ruim para o sistema.

**Como o wait() resolve?**
Quando uma thread executa wait():
Ela para completamente; É removida da CPU; Fica “dormindo” dentro do objeto monitor; Não consome processamento e Fica esperando até ser acordada pelo *notify() ou notifyAll()*.

Quando o cancelamento executa:

`assentosDisponiveis++;
notifyAll();`
*Todas as threads dormindo são acordadas de forma ordenada.*

**Benefícios diretos:**

- Zero uso de CPU enquanto espera;
- Nenhum loop infinito;
- Processo altamente eficiente;
- Comunicação direta entre threads produtor/consumidor.



