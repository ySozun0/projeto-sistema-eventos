// SistemaEventosApp.java
package br.com.eventos.app;

import br.com.eventos.CategoriaEvento;
import br.com.eventos.modelo.Evento;
import br.com.eventos.modelo.Usuario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SistemaEventosApp {

    private List<Usuario> usuarios;
    private List<Evento> eventos;
    private Usuario usuarioLogado;
    private Scanner scanner;
    private static final String NOME_ARQUIVO_EVENTOS = "events.data";
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SistemaEventosApp() {
        this.usuarios = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.usuarioLogado = null;
        this.scanner = new Scanner(System.in);
        carregarEventosDeArquivo();
    }

    public static void main(String[] args) {
        SistemaEventosApp app = new SistemaEventosApp();
        app.iniciar();
    }

    public void iniciar() {
        System.out.println("Bem-vindo ao Sistema de Gerenciamento de Eventos da sua Cidade!");
        int escolha;
        do {
            if (usuarioLogado == null) {
                exibirMenuDeslogado();
                escolha = lerOpcaoMenu(3);
            } else {
                exibirMenuLogado();
                escolha = lerOpcaoMenu(10);
            }
            processarEscolha(escolha);
        } while (escolha != 0);

        salvarEventosEmArquivo();
        System.out.println("Obrigado por usar o sistema. Até logo!");
        scanner.close();
    }

    private void exibirMenuDeslogado() {
        System.out.println("\n--- Menu Principal (Deslogado) ---");
        System.out.println("1. Cadastrar Usuário");
        System.out.println("2. Fazer Login");
        System.out.println("3. Consultar Todos os Eventos");
        System.out.println("0. Sair do Sistema");
        System.out.print("Escolha uma opção: ");
    }

    private void exibirMenuLogado() {
        System.out.println("\n--- Menu Principal (Logado como: " + usuarioLogado.getNome() + ") ---");
        System.out.println("1. Cadastrar Novo Evento");
        System.out.println("2. Consultar Todos os Eventos (Ordenados por Proximidade)");
        System.out.println("3. Participar de um Evento");
        System.out.println("4. Visualizar Eventos que Estou Participando");
        System.out.println("5. Cancelar Participação em Evento");
        System.out.println("6. Consultar Eventos Ocorrendo Agora");
        System.out.println("7. Consultar Eventos Passados");
        System.out.println("8. Ver meus dados de usuário");
        System.out.println("9. Fazer Logout");
        System.out.println("0. Salvar e Sair do Sistema");
        System.out.print("Escolha uma opção: ");
    }

    private int lerOpcaoMenu(int maxOpcao) {
        int opcao = -1;
        try {
            opcao = scanner.nextInt();
            scanner.nextLine();
            if (opcao < 0 || opcao > maxOpcao) {
                System.out.println("Opção inválida. Tente novamente.");
                return -1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, insira um número.");
            scanner.nextLine();
            return -1;
        }
        return opcao;
    }

    private void processarEscolha(int escolha) {
        if (usuarioLogado == null) {
            switch (escolha) {
                case 1:
                    cadastrarUsuario();
                    break;
                case 2:
                    fazerLogin();
                    break;
                case 3:
                    consultarEventos(false);
                    break;
                case 0:
                    break;
                default:
                    if (escolha != -1) System.out.println("Opção inválida.");
                    break;
            }
        } else {
            switch (escolha) {
                case 1:
                    cadastrarEvento();
                    break;
                case 2:
                    consultarEventos(true);
                    break;
                case 3:
                    participarDeEvento();
                    break;
                case 4:
                    visualizarMeusEventos();
                    break;
                case 5:
                    cancelarParticipacaoEvento();
                    break;
                case 6:
                    consultarEventosOcorrendoAgora();
                    break;
                case 7:
                    consultarEventosPassados();
                    break;
                case 8:
                    System.out.println(usuarioLogado);
                    break;
                case 9:
                    fazerLogout();
                    break;
                case 0:
                    break;
                default:
                    if (escolha != -1) System.out.println("Opção inválida.");
                    break;
            }
        }
    }

    private void cadastrarUsuario() {
        System.out.println("\n--- Cadastro de Usuário ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();

        if (usuarios.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email))) {
            System.out.println("Erro: Email já cadastrado. Tente outro.");
            return;
        }

        Usuario novoUsuario = new Usuario(nome, email, cidade);
        usuarios.add(novoUsuario);
        System.out.println("Usuário " + nome + " cadastrado com sucesso!");
    }

    private void fazerLogin() {
        System.out.println("\n--- Login de Usuário ---");
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado. Por favor, cadastre um usuário primeiro.");
            return;
        }
        System.out.print("Digite seu email para login: ");
        String email = scanner.nextLine();

        Optional<Usuario> usuarioOpt = usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();

        if (usuarioOpt.isPresent()) {
            usuarioLogado = usuarioOpt.get();
            System.out.println("Login bem-sucedido! Bem-vindo(a), " + usuarioLogado.getNome() + ".");
        } else {
            System.out.println("Email não encontrado. Verifique o email ou cadastre-se.");
        }
    }

    private void fazerLogout() {
        System.out.println(usuarioLogado.getNome() + " deslogado com sucesso.");
        usuarioLogado = null;
    }

    private void cadastrarEvento() {
        System.out.println("\n--- Cadastro de Novo Evento ---");
        System.out.print("Nome do Evento: ");
        String nome = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        CategoriaEvento categoria = null;
        while (categoria == null) {
            CategoriaEvento.mostrarOpcoes();
            System.out.print("Escolha a categoria (número): ");
            try {
                int escolhaCat = scanner.nextInt();
                scanner.nextLine();
                categoria = CategoriaEvento.fromEscolha(escolhaCat);
                if (categoria == null) {
                    System.out.println("Categoria inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida para categoria. Use números.");
                scanner.nextLine();
            }
        }

        LocalDateTime horario = null;
        while (horario == null) {
            System.out.print("Horário (dd/MM/yyyy HH:mm): ");
            String strHorario = scanner.nextLine();
            try {
                horario = LocalDateTime.parse(strHorario, INPUT_DATE_TIME_FORMATTER);
                if (horario.isBefore(LocalDateTime.now())) {
                    System.out.println("Não é possível cadastrar eventos com data/hora no passado. Tente novamente.");
                    horario = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data/hora inválido. Use dd/MM/yyyy HH:mm. Ex: 25/12/2025 14:30");
            }
        }

        System.out.print("Descrição do Evento: ");
        String descricao = scanner.nextLine();

        Evento novoEvento = new Evento(nome, endereco, categoria, horario, descricao);
        eventos.add(novoEvento);
        ordenarEventosPorProximidade();
        System.out.println("Evento '" + nome + "' cadastrado com sucesso!");
    }
    
    private void ordenarEventosPorProximidade() {
        eventos.sort(Comparator.comparing(Evento::getHorario));
    }

    private void consultarEventos(boolean logado) {
        System.out.println("\n--- Todos os Eventos Cadastrados ---");
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado no momento.");
            return;
        }
        
        ordenarEventosPorProximidade();

        for (int i = 0; i < eventos.size(); i++) {
            Evento evento = eventos.get(i);
            System.out.println("\n[" + (i + 1) + "] " + evento.getNome() + " (" + evento.getCategoria().getDescricao() + ")");
            System.out.println("  Horário: " + evento.getHorarioFormatado());
            System.out.println("  Local: " + evento.getEndereco());
            System.out.println("  Descrição: " + evento.getDescricao());
            if(evento.jaOcorreu()){
                System.out.println("  Status: Já Ocorreu");
            } else if (evento.estaOcorrendoAgora()){
                System.out.println("  Status: Ocorrendo Agora!");
            } else {
                 System.out.println("  Status: Próximo Evento");
            }
            System.out.println("  Participantes confirmados: " + evento.getParticipantes().size());
        }
    }

    private void participarDeEvento() {
        System.out.println("\n--- Participar de Evento ---");
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento disponível para participação.");
            return;
        }

        consultarEventos(true);
        System.out.print("Digite o número do evento que deseja participar (ou 0 para cancelar): ");
        try {
            int escolha = scanner.nextInt();
            scanner.nextLine();

            if (escolha == 0) return;

            if (escolha > 0 && escolha <= eventos.size()) {
                Evento eventoEscolhido = eventos.get(escolha - 1);
                if (eventoEscolhido.jaOcorreu()) {
                    System.out.println("Não é possível participar de um evento que já ocorreu.");
                    return;
                }
                if (eventoEscolhido.adicionarParticipante(usuarioLogado)) {
                    System.out.println("Presença confirmada no evento: " + eventoEscolhido.getNome());
                } else {
                    System.out.println("Você já está participando deste evento.");
                }
            } else {
                System.out.println("Número de evento inválido.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Use números.");
            scanner.nextLine();
        }
    }

    private void visualizarMeusEventos() {
        System.out.println("\n--- Meus Eventos (Participação Confirmada) ---");
        List<Evento> meusEventos = eventos.stream()
                .filter(e -> e.getParticipantes().contains(usuarioLogado))
                .sorted(Comparator.comparing(Evento::getHorario))
                .collect(Collectors.toList());

        if (meusEventos.isEmpty()) {
            System.out.println("Você não confirmou presença em nenhum evento.");
            return;
        }

        for (int i = 0; i < meusEventos.size(); i++) {
            Evento evento = meusEventos.get(i);
             System.out.println("\n[" + (i + 1) + "] " + evento.getNome() + " (" + evento.getCategoria().getDescricao() + ")");
            System.out.println("  Horário: " + evento.getHorarioFormatado());
            System.out.println("  Local: " + evento.getEndereco());
             if(evento.jaOcorreu()){
                System.out.println("  Status: Já Ocorreu");
            } else if (evento.estaOcorrendoAgora()){
                System.out.println("  Status: Ocorrendo Agora!");
            }
        }
    }

    private void cancelarParticipacaoEvento() {
        System.out.println("\n--- Cancelar Participação em Evento ---");
        List<Evento> meusEventos = eventos.stream()
                .filter(e -> e.getParticipantes().contains(usuarioLogado) && !e.jaOcorreu())
                .sorted(Comparator.comparing(Evento::getHorario))
                .collect(Collectors.toList());

        if (meusEventos.isEmpty()) {
            System.out.println("Você não está participando de nenhum evento futuro ou que esteja ocorrendo.");
            return;
        }

        System.out.println("Eventos que você está participando (e pode cancelar):");
        for (int i = 0; i < meusEventos.size(); i++) {
            System.out.println((i + 1) + ". " + meusEventos.get(i).getNome() + " - " + meusEventos.get(i).getHorarioFormatado());
        }

        System.out.print("Digite o número do evento para cancelar participação (ou 0 para voltar): ");
        try {
            int escolha = scanner.nextInt();
            scanner.nextLine();

            if (escolha == 0) return;

            if (escolha > 0 && escolha <= meusEventos.size()) {
                Evento eventoEscolhido = meusEventos.get(escolha - 1);
                if (eventoEscolhido.removerParticipante(usuarioLogado)) {
                    System.out.println("Participação cancelada no evento: " + eventoEscolhido.getNome());
                } else {
                    System.out.println("Não foi possível cancelar a participação. Você não estava na lista.");
                }
            } else {
                System.out.println("Número de evento inválido.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Use números.");
            scanner.nextLine();
        }
    }

    private void consultarEventosOcorrendoAgora() {
        System.out.println("\n--- Eventos Ocorrendo Agora ---");
        List<Evento> eventosAgora = eventos.stream()
                .filter(Evento::estaOcorrendoAgora)
                .sorted(Comparator.comparing(Evento::getHorario))
                .collect(Collectors.toList());

        if (eventosAgora.isEmpty()) {
            System.out.println("Nenhum evento ocorrendo no momento.");
            return;
        }
        eventosAgora.forEach(System.out::println);
    }

    private void consultarEventosPassados() {
        System.out.println("\n--- Eventos Que Já Ocorreram ---");
        List<Evento> eventosPassados = eventos.stream()
                .filter(Evento::jaOcorreu)
                .sorted(Comparator.comparing(Evento::getHorario).reversed())
                .collect(Collectors.toList());

        if (eventosPassados.isEmpty()) {
            System.out.println("Nenhum evento ocorreu ainda ou foi registrado como passado.");
            return;
        }
        eventosPassados.forEach(System.out::println);
    }

    private void salvarEventosEmArquivo() {
        File arquivo = new File(NOME_ARQUIVO_EVENTOS);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (Evento evento : eventos) {
                writer.write(evento.toFileString());
                writer.newLine();
            }
            // System.out.println("Eventos salvos em " + NOME_ARQUIVO_EVENTOS); // Removido para ser mais discreto
        } catch (IOException e) {
            System.err.println("Erro ao salvar eventos no arquivo: " + e.getMessage());
        }
    }

    private void carregarEventosDeArquivo() {
        File arquivo = new File(NOME_ARQUIVO_EVENTOS);
        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Evento evento = Evento.fromFileString(linha);
                if (evento != null) {
                    boolean jaExiste = eventos.stream().anyMatch(e -> 
                        e.getNome().equals(evento.getNome()) && e.getHorario().equals(evento.getHorario())
                    );
                    if (!jaExiste) {
                        eventos.add(evento);
                    }
                }
            }
            ordenarEventosPorProximidade();
            // System.out.println(eventosCarregadosCount + " novos eventos carregados de " + NOME_ARQUIVO_EVENTOS); // Removido para ser mais discreto
        } catch (IOException e) {
            System.err.println("Erro ao carregar eventos do arquivo: " + e.getMessage());
        }
    }
}
