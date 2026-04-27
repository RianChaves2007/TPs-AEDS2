import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * TP2 - Exercicio 11
 * Lista de registros (Restaurante) com alocacao sequencial.
 * Estrutura baseada na Lista de inteiros vista em sala de aula
 * (referencias_codigo/Lista.java).
 */
public class Lista {
    private Restaurante[] lista;
    private int tam;

    public Lista() {
        this(600);
    }

    public Lista(int tamanho) {
        lista = new Restaurante[tamanho];
        tam = 0;
    }

    public int getTam() {
        return tam;
    }

    public Restaurante getItemLista(int i) {
        return lista[i];
    }

    /* ===== INSERCOES ===== */

    public void inserirInicio(Restaurante dado) throws Exception {
        if (tam >= lista.length) {
            throw new Exception("Erro ao inserir!");
        }
        for (int i = tam; i > 0; i--) {
            lista[i] = lista[i - 1];
        }
        lista[0] = dado;
        tam++;
    }

    public void inserirFim(Restaurante dado) throws Exception {
        if (tam >= lista.length) {
            throw new Exception("Erro ao inserir!");
        }
        lista[tam] = dado;
        tam++;
    }

    public void inserir(Restaurante dado, int pos) throws Exception {
        if (tam >= lista.length || pos < 0 || pos > tam) {
            throw new Exception("Erro ao inserir!");
        }
        for (int i = tam; i > pos; i--) {
            lista[i] = lista[i - 1];
        }
        lista[pos] = dado;
        tam++;
    }

    /* ===== REMOCOES ===== */

    public Restaurante removerInicio() throws Exception {
        if (tam == 0) {
            throw new Exception("Erro ao remover!");
        }
        Restaurante resp = lista[0];
        tam--;
        for (int i = 0; i < tam; i++) {
            lista[i] = lista[i + 1];
        }
        return resp;
    }

    public Restaurante removerFim() throws Exception {
        if (tam == 0) {
            throw new Exception("Erro ao remover!");
        }
        return lista[--tam];
    }

    public Restaurante remover(int pos) throws Exception {
        if (tam == 0 || pos < 0 || pos >= tam) {
            throw new Exception("Erro ao remover!");
        }
        Restaurante resp = lista[pos];
        tam--;
        for (int i = pos; i < tam; i++) {
            lista[i] = lista[i + 1];
        }
        return resp;
    }

    /* ===== AUXILIARES ===== */

    public void mostrar() {
        for (int i = 0; i < tam; i++) {
            System.out.println(lista[i].formatar());
        }
    }

    public boolean pesquisar(int id) {
        boolean retorno = false;
        for (int i = 0; i < tam && !retorno; i++) {
            retorno = (lista[i].getId() == id);
        }
        return retorno;
    }

    /* ===== PRINCIPAL ===== */
    public static void main(String[] args) throws Exception {
        ColecaoRestaurantes col = ColecaoRestaurantes.lerCsv();
        Scanner sc = new Scanner(System.in);
        Lista lista = new Lista(600);

        // Parte 1: ids ate -1, inseridos no fim
        while (sc.hasNext()) {
            int id = sc.nextInt();
            if (id == -1) break;
            Restaurante r = col.buscarPorId(id);
            if (r != null) lista.inserirFim(r);
        }

        // Parte 2: n comandos de insercao/remocao
        if (sc.hasNext()) {
            int qtd = sc.nextInt();
            for (int k = 0; k < qtd; k++) {
                if (!sc.hasNext()) break;
                String cmd = sc.next();
                Restaurante removido = null;
                switch (cmd) {
                    case "II": {
                        int id = sc.nextInt();
                        Restaurante r = col.buscarPorId(id);
                        if (r != null) lista.inserirInicio(r);
                        break;
                    }
                    case "I*": {
                        int pos = sc.nextInt();
                        int id = sc.nextInt();
                        Restaurante r = col.buscarPorId(id);
                        if (r != null) lista.inserir(r, pos);
                        break;
                    }
                    case "IF": {
                        int id = sc.nextInt();
                        Restaurante r = col.buscarPorId(id);
                        if (r != null) lista.inserirFim(r);
                        break;
                    }
                    case "RI":
                        removido = lista.removerInicio();
                        break;
                    case "R*": {
                        int pos = sc.nextInt();
                        removido = lista.remover(pos);
                        break;
                    }
                    case "RF":
                        removido = lista.removerFim();
                        break;
                    default:
                        break;
                }
                if (removido != null) {
                    System.out.println("(R)" + removido.getNome());
                }
            }
        }

        // Saida final: todos os registros presentes na lista, do primeiro ao ultimo
        lista.mostrar();
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