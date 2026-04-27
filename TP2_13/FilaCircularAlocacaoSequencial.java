import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * TP2 - Exercicio 13
 * Fila Circular de registros (Restaurante) com alocacao sequencial,
 * tamanho cinco. Estrutura baseada na fila de inteiros vista em sala
 * de aula (referencias_codigo/Fila.java).
 */
public class FilaCircularAlocacaoSequencial {
    private Restaurante[] array;
    private int primeiro;
    private int ultimo;

    public FilaCircularAlocacaoSequencial() {
        this(5);
    }

    public FilaCircularAlocacaoSequencial(int tamanho) {
        array = new Restaurante[tamanho + 1];
        primeiro = ultimo = 0;
    }

    public boolean cheia() {
        return ((ultimo + 1) % array.length) == primeiro;
    }

    public boolean vazia() {
        return primeiro == ultimo;
    }

    public void inserir(Restaurante r) throws Exception {
        if (cheia()) {
            throw new Exception("Erro ao inserir!");
        }
        array[ultimo] = r;
        ultimo = (ultimo + 1) % array.length;
    }

    public Restaurante remover() throws Exception {
        if (vazia()) {
            throw new Exception("Erro ao remover!");
        }
        Restaurante resp = array[primeiro];
        primeiro = (primeiro + 1) % array.length;
        return resp;
    }

    /**
     * Media arredondada do ano de abertura dos registros presentes
     * na fila no momento da chamada.
     */
    public int mediaAnoArredondada() {
        int soma = 0;
        int count = 0;
        for (int i = primeiro; i != ultimo; i = (i + 1) % array.length) {
            soma += array[i].getDataAbertura().getAno();
            count++;
        }
        if (count == 0) return 0;
        return (int) Math.round((double) soma / (double) count);
    }

    public void mostrar() {
        for (int i = primeiro; i != ultimo; i = (i + 1) % array.length) {
            System.out.println(array[i].formatar());
        }
    }

    /* ===== PRINCIPAL ===== */
    public static void main(String[] args) throws Exception {
        ColecaoRestaurantes col = ColecaoRestaurantes.lerCsv();
        Scanner sc = new Scanner(System.in);
        FilaCircularAlocacaoSequencial fila = new FilaCircularAlocacaoSequencial(5);

        // Parte 1: ids ate -1. Cada insercao imprime (I)media.
        // Se a fila estiver cheia, remove antes (e imprime (R)nome).
        while (sc.hasNext()) {
            int id = sc.nextInt();
            if (id == -1) break;
            Restaurante r = col.buscarPorId(id);
            if (r != null) {
                if (fila.cheia()) {
                    Restaurante removido = fila.remover();
                    System.out.println("(R)" + removido.getNome());
                }
                fila.inserir(r);
                System.out.println("(I)" + fila.mediaAnoArredondada());
            }
        }

        // Parte 2: n comandos I/R
        if (sc.hasNext()) {
            int qtd = sc.nextInt();
            for (int k = 0; k < qtd; k++) {
                if (!sc.hasNext()) break;
                String cmd = sc.next();
                if (cmd.equals("I")) {
                    int id = sc.nextInt();
                    Restaurante r = col.buscarPorId(id);
                    if (r != null) {
                        if (fila.cheia()) {
                            Restaurante removido = fila.remover();
                            System.out.println("(R)" + removido.getNome());
                        }
                        fila.inserir(r);
                        System.out.println("(I)" + fila.mediaAnoArredondada());
                    }
                } else if (cmd.equals("R")) {
                    Restaurante removido = fila.remover();
                    System.out.println("(R)" + removido.getNome());
                }
            }
        }

        // Saida final: registros presentes na fila, do primeiro ao ultimo
        fila.mostrar();
        sc.close();
    }
}

/* ===== MODELAGEM (mesma de TP2_01) ===== */

class ColecaoRestaurantes {
    private int tamanho;
    private Restaurante[] restaurantes;

    public ColecaoRestaurantes(int tamanho) {
        this.tamanho = 0;
        this.restaurantes = new Restaurante[tamanho];
    }

    public ColecaoRestaurantes() {
        this(500);
    }

    public Restaurante[] getRestaurantes() {
        return restaurantes;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void extender() {
        Restaurante[] novoArray = new Restaurante[this.restaurantes.length * 2];
        for (int i = 0; i < this.restaurantes.length; i++) {
            novoArray[i] = this.restaurantes[i];
        }
        this.restaurantes = novoArray;
    }

    public void inserirNoFim(Restaurante dado) {
        if (tamanho >= this.restaurantes.length) {
            extender();
        }
        restaurantes[this.tamanho] = dado;
        tamanho++;
    }

    public Restaurante buscarPorId(int id) {
        for (int i = 0; i < tamanho; i++) {
            if (restaurantes[i].getId() == id) {
                return restaurantes[i];
            }
        }
        return null;
    }

    public static ColecaoRestaurantes lerCsv() {
        ColecaoRestaurantes col = new ColecaoRestaurantes();
        String path = "/tmp/restaurantes.csv";
        File myObj = new File(path);
        try (Scanner myReader = new Scanner(myObj)) {
            myReader.nextLine();
            while (myReader.hasNextLine()) {
                String linha = myReader.nextLine();
                Restaurante rest = Restaurante.parseRestaurante(linha);
                col.inserirNoFim(rest);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return col;
    }

    public ColecaoRestaurantes lerCsv(String path) {
        ColecaoRestaurantes col = new ColecaoRestaurantes();
        File myObj = new File(path);
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                String linha = myReader.nextLine();
                Restaurante rest = Restaurante.parseRestaurante(linha);
                col.inserirNoFim(rest);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return col;
    }
}

class Restaurante {
    private int id;
    private String nome;
    private String cidade;
    private int capacidade;
    private double avaliacao;
    private String[] tiposCozinha;
    private int faixaPreco;
    private Hora horarioAbertura;
    private Hora horarioFechamento;
    private Data dataAbertura;
    private boolean aberto;

    public Restaurante() {}

    public Restaurante(int id, String nome, String cidade, int capacidade,
                       double avaliacao, String[] tiposCozinha, int faixaPreco,
                       Hora horarioAbertura, Hora horarioFechamento,
                       Data dataAbertura, boolean aberto) {
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
        this.capacidade = capacidade;
        this.avaliacao = avaliacao;
        this.tiposCozinha = tiposCozinha;
        this.faixaPreco = faixaPreco;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
        this.dataAbertura = dataAbertura;
        this.aberto = aberto;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCidade() { return cidade; }
    public int getCapacidade() { return capacidade; }
    public double getAvaliacao() { return avaliacao; }
    public String[] getTiposCozinha() { return tiposCozinha; }
    public int getFaixaPreco() { return faixaPreco; }
    public Hora getHorarioAbertura() { return horarioAbertura; }
    public Hora getHorarioFechamento() { return horarioFechamento; }
    public Data getDataAbertura() { return dataAbertura; }
    public boolean isAberto() { return aberto; }

    public static Restaurante parseRestaurante(String s) {
        String[] campos = new String[10];
        int i = 0, f = 0, cont = 0;
        int tamF = s.length() - 1;
        while (f <= tamF) {
            if (s.charAt(f) == ',') {
                campos[cont++] = s.substring(i, f);
                i = f + 1;
            }
            f++;
        }
        campos[cont++] = s.substring(i);

        int id = Integer.parseInt(campos[0]);
        String nome = campos[1];
        String cidade = campos[2];
        int capacidade = Integer.parseInt(campos[3]);
        double avaliacao = Double.parseDouble(campos[4]);
        int faixaPreco = campos[6].length();
        boolean aberto = campos[9].equals("true");

        String tipos = campos[5];
        int qtd = 1;
        for (int k = 0; k < tipos.length(); k++) {
            if (tipos.charAt(k) == ';') qtd++;
        }
        String[] tiposCozinha = new String[qtd];
        i = 0; f = 0; cont = 0;
        tamF = tipos.length() - 1;
        while (f <= tamF) {
            if (tipos.charAt(f) == ';') {
                tiposCozinha[cont++] = tipos.substring(i, f);
                i = f + 1;
            }
            f++;
        }
        tiposCozinha[cont++] = tipos.substring(i);

        String horario = campos[7];
        Hora horarioAbertura = Hora.parseHora(horario.substring(0, 5));
        Hora horarioFechamento = Hora.parseHora(horario.substring(6, 11));

        Data dataAbertura = Data.parseData(campos[8]);

        return new Restaurante(id, nome, cidade, capacidade, avaliacao,
                tiposCozinha, faixaPreco, horarioAbertura, horarioFechamento,
                dataAbertura, aberto);
    }

    public String formatar() {
        String fmt = "[%d ## %s ## %s ## %d ## %s ## [%s] ## %s ## %s-%s ## %s ## %b]";
        String cozinha = "";
        int nCozinha = this.tiposCozinha.length;
        for (int i = 0; i < nCozinha; i++) {
            if (i + 1 < nCozinha) {
                cozinha = cozinha + this.tiposCozinha[i] + ",";
            } else {
                cozinha += this.tiposCozinha[i];
            }
        }
        String preco = "";
        for (int i = 0; i < this.faixaPreco; i++) preco += "$";
        return String.format(fmt,
                this.id, this.nome, this.cidade, this.capacidade,
                String.valueOf(this.avaliacao),
                cozinha, preco,
                this.horarioAbertura.formatar(),
                this.horarioFechamento.formatar(),
                this.dataAbertura.formatar(),
                this.aberto);
    }
}

class Data {
    private int ano;
    private int mes;
    private int dia;

    public Data(int ano, int mes, int dia) {
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }

    public int getAno() { return ano; }
    public int getMes() { return mes; }
    public int getDia() { return dia; }

    public static Data parseData(String s) {
        int ano = Integer.parseInt(s.substring(0, 4));
        int mes = Integer.parseInt(s.substring(5, 7));
        int dia = Integer.parseInt(s.substring(8, 10));
        return new Data(ano, mes, dia);
    }

    public String formatar() {
        return String.format("%02d/%02d/%04d", dia, mes, ano);
    }
}

class Hora {
    private int hora;
    private int minuto;

    public Hora(int hora, int minuto) {
        this.hora = hora;
        this.minuto = minuto;
    }

    public int getHora() { return hora; }
    public int getMinuto() { return minuto; }

    public static Hora parseHora(String s) {
        int hora = Integer.parseInt(s.substring(0, 2));
        int minuto = Integer.parseInt(s.substring(3, 5));
        return new Hora(hora, minuto);
    }

    public String formatar() {
        return String.format("%02d:%02d", hora, minuto);
    }
}