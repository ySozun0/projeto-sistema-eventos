// Evento.java
package br.com.eventos.modelo;

import br.com.eventos.CategoriaEvento;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Evento {
    private String nome;
    private String endereco;
    private CategoriaEvento categoria;
    private LocalDateTime horario;
    private String descricao;
    private List<Usuario> participantes;

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final String SEPARADOR_ARQUIVO = ";;";

    public Evento(String nome, String endereco, CategoriaEvento categoria, LocalDateTime horario, String descricao) {
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
        this.participantes = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public CategoriaEvento getCategoria() {
        return categoria;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<Usuario> getParticipantes() {
        return participantes;
    }

    public boolean adicionarParticipante(Usuario usuario) {
        if (!this.participantes.contains(usuario)) {
            this.participantes.add(usuario);
            return true;
        }
        return false;
    }

    public boolean removerParticipante(Usuario usuario) {
        return this.participantes.remove(usuario);
    }

    public boolean estaOcorrendoAgora() {
        LocalDateTime agora = LocalDateTime.now();
        return horario.toLocalDate().isEqual(agora.toLocalDate()) &&
               !horario.isAfter(agora);
    }

    public boolean jaOcorreu() {
        return horario.isBefore(LocalDateTime.now());
    }

    public String getHorarioFormatado() {
        return horario.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"));
    }

    @Override
    public String toString() {
        return "Evento: " + nome + "\n" +
               "  Endereço: " + endereco + "\n" +
               "  Categoria: " + categoria.getDescricao() + "\n" +
               "  Horário: " + getHorarioFormatado() + "\n" +
               "  Descrição: " + descricao + "\n" +
               "  Participantes: " + participantes.size();
    }

    public String toFileString() {
        return String.join(SEPARADOR_ARQUIVO,
                nome,
                endereco,
                categoria.name(),
                horario.format(DATE_TIME_FORMATTER),
                descricao);
    }

    public static Evento fromFileString(String linha) {
        String[] partes = linha.split(SEPARADOR_ARQUIVO);
        if (partes.length == 5) {
            try {
                String nome = partes[0];
                String endereco = partes[1];
                CategoriaEvento categoria = CategoriaEvento.valueOf(partes[2]);
                LocalDateTime horario = LocalDateTime.parse(partes[3], DATE_TIME_FORMATTER);
                String descricao = partes[4];
                return new Evento(nome, endereco, categoria, horario, descricao);
            } catch (DateTimeParseException e) {
                System.err.println("Erro ao parsear data e hora do evento: " + partes[3] + " - " + e.getMessage());
                return null;
            } catch (IllegalArgumentException e) {
                System.err.println("Erro ao parsear categoria do evento: " + partes[2] + " - " + e.getMessage());
                return null;
            }
        }
        System.err.println("Linha de evento mal formatada no arquivo: " + linha);
        return null;
    }
}