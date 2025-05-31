// CategoriaEvento.java
package br.com.eventos;

public enum CategoriaEvento {
    FESTA("Festa"),
    ESPORTIVO("Evento Esportivo"),
    SHOW("Show"),
    CONFERENCIA("Conferência"),
    WORKSHOP("Workshop"),
    OUTRO("Outro");

    private final String descricao;

    CategoriaEvento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static void mostrarOpcoes() {
        System.out.println("Categorias Disponíveis:");
        for (int i = 0; i < values().length; i++) {
            System.out.println((i + 1) + ". " + values()[i].getDescricao());
        }
    }

    public static CategoriaEvento fromEscolha(int escolha) {
        if (escolha > 0 && escolha <= values().length) {
            return values()[escolha - 1];
        }
        return null;
    }
}