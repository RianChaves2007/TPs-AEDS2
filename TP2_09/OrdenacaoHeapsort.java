import java.io.*;
import java.util.Scanner;

public class OrdenacaoHeapsort {

    static final String MATRICULA = "898910";
    static long comparacoes = 0;
    static long movimentacoes = 0;
    static Restaurante[] arr;

    static int cmp(Restaurante a, Restaurante b) {
        comparacoes++;
        Data da = a.getDataAbertura(), db = b.getDataAbertura();
        if (da.getAno() != db.getAno()) return da.getAno() - db.getAno();
        if (da.getMes() != db.getMes()) return da.getMes() - db.getMes();
        if (da.getDia() != db.getDia()) return da.getDia() - db.getDia();
        return a.getNome().compareTo(b.getNome());
    }

    static void swap(int i, int j) {
        Restaurante t = arr[i]; arr[i] = arr[j]; arr[j] = t;
        movimentacoes += 3;
    }

    static void construir(int tamHeap) {
        for (int i = tamHeap; i > 1 && cmp(arr[i], arr[i / 2]) > 0; i /= 2) {
            swap(i, i / 2);
        }
    }

    static int getMaiorFilho(int i, int tamHeap) {
        if (2 * i == tamHeap || cmp(arr[2 * i], arr[2 * i + 1]) > 0) return 2 * i;
        return 2 * i + 1;
    }

    static void reconstruir(int tamHeap) {
        int i = 1;
        while (i <= tamHeap / 2) {
            int filho = getMaiorFilho(i, tamHeap);
            if (cmp(arr[i], arr[filho]) < 0) {
                swap(i, filho);
                i = filho;
            } else i = tamHeap;
        }
    }

    static void heapsort(int n) {
        Restaurante[] tmp = new Restaurante[n + 1];
        for (int i = 0; i < n; i++) tmp[i + 1] = arr[i];
        arr = tmp;
        for (int t = 2; t <= n; t++) construir(t);
        int tamHeap = n;
        while (tamHeap > 1) { swap(1, tamHeap--); reconstruir(tamHeap); }
        Restaurante[] out = new Restaurante[n];
        for (int i = 0; i < n; i++) out[i] = arr[i + 1];
        arr = out;
    }

    /* === PRINCIPAL === */
    public static void main(String[] args) throws IOException {
        ColecaoRestaurantes col = ColecaoRestaurantes.lerCsv();
        Scanner sc = new Scanner(System.in);
        arr = new Restaurante[500];
        int n = 0;
        int id;
        while (sc.hasNext() && (id = sc.nextInt()) != -1) {
            Restaurante r = col.buscarPorId(id);
            if (r != null) arr[n++] = r;
        }
        sc.close();
        Restaurante[] tmp = new Restaurante[n];
        for (int i = 0; i < n; i++) tmp[i] = arr[i];
        arr = tmp;

        long ini = System.nanoTime();
        if (n > 1) heapsort(n);
        long fim = System.nanoTime();
        double tempo = (fim - ini) / 1e9;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(arr[i].formatar()).append('\n');
        System.out.print(sb);

        try (PrintWriter pw = new PrintWriter(new FileWriter(MATRICULA + "_heapsort.txt"))) {
            pw.printf("%s\t%d\t%d\t%.6f%n", MATRICULA, comparacoes, movimentacoes, tempo);
        }
    }

    /* === MODELAGEM === */
    public static class ColecaoRestaurantes {
        private int tamanho;
        private Restaurante[] restaurantes;

        public ColecaoRestaurantes(int tamanho){
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

        public Restaurante buscarPorId(int id) {
            for (int i = 0; i < tamanho; i++) {
                if (restaurantes[i].getId() == id) {
                    return restaurantes[i];
                }
            }
            return null;
        }

        public void extender(){
            Restaurante[] novoArray = new Restaurante[this.restaurantes.length * 2]; 
            for (int i = 0; i < this.restaurantes.length; i++) {
                novoArray[i] = this.restaurantes[i]; 
            }
            this.restaurantes = novoArray; 
        }

        public void inserirNoFim(Restaurante dado) {
            if (this.tamanho >= this.restaurantes.length) {
                this.extender();
            }

            this.restaurantes[this.tamanho] = dado;
            this.tamanho++;
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

    static class Restaurante {
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

        public Restaurante(
            int id,
            String nome,
            String cidade,
            int capacidade,
            double avaliacao,
            String[] tiposCozinha,
            int faixaPreco,
            Hora horarioAbertura,
            Hora horarioFechamento,
            Data dataAbertura,
            boolean aberto
        ) {
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
            int tamF = s.length()-1;
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
            tamF = tipos.length()-1;
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
            String restaurantesFormatados = "[%d ## %s ## %s ## %d ## %s ## [%s] ## %s ## %s-%s ## %s ## %b]";

            String cozinha = "";
            int nCozinha = this.tiposCozinha.length;
            for(int i=0; i<nCozinha; i++){
                if(i+1 < nCozinha){
                    cozinha = cozinha + this.tiposCozinha[i] + ",";
                }else{
                    cozinha += this.tiposCozinha[i];    
                }
            }

            String preco = "";
            for(int i=0; i<this.faixaPreco; i++){
                preco += "$";
            }

            return String.format(restaurantesFormatados,
                this.id,
                this.nome,
                this.cidade,
                this.capacidade,
                String.valueOf(this.avaliacao),
                cozinha,
                preco,
                this.horarioAbertura.formatar(),
                this.horarioFechamento.formatar(),
                this.dataAbertura.formatar(),
                this.aberto
            );
        }
    }

    public static class Data {
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

    public static class Hora {
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
}