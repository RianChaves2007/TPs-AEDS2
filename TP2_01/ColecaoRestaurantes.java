import java.io.File;  // Import the File class

import javax.xml.crypto.Data;

public class ColecaoRestaurantes{
    //atributos da superclasse
    private int tamanho;
    private Restaurante restaurantes;

    public Restaurante[] getRestaurantes() {
        // 
    }
    public int getTamanho() {
        return tamanho;
    }
    public static ColecaoRestaurantes lerCsv(){
        //  
    }

    public void lerCsv(String path){

    }

    public class Restaurante extends ColecaoRestaurantes{
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

        public Restaurante(){}

        // construtor de Restaurante
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
        ){
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

        public static Restaurante parseRestaurante(String s){
            // s no formato: id,nome,cidade,capacidade,avaliacao,tipos_cozinha,faixa_preco,horario,data_abertura,aberto
            int id;
            String nome;
            String cidade;
            int capacidade;
            double avaliacao;
            String[] tiposCozinha;
            int faixaPreco;
            Hora horarioAbertura;
            Hora horarioFechamento;
            Data dataAbertura;
            boolean aberto;

            // sem utilizar .split()
            int i = 0, f = 0, cont = 0;
            while(f < s.length()){
                while(s.charAt(f++) != ',');
                swich(cont){
                    case 0: //id
                        id = Integer.parseInt(s.substring(i, f-1));
                        break;
                }
            }

        }

        public String formatar(){

        }
    }

    public class Data{
        private int ano;
        private int mes;
        private int dia;

        public Data(int ano, int mes, int dia){
            this.ano = ano;
            this.mes = mes;
            this.dia = dia;
        }

        //acesso aos atributos
        public int getAno() {
            return ano;
        }
        public int getDia() {
            return dia;
        }
        public int getMes() {
            return mes;
        }
        // julgo que o 'set' não seja necessário por enquanto

        // processamento do tipo data
        public static Data parseData(String s){
            // s no formato YYYY-MM-DD
            int ano = Integer.parseInt(s.substring(0,4));
            int mes = Integer.parseInt(s.substring(6,7));
            int dia = Integer.parseInt(s.substring(9, 10));
            return new Data(ano, mes, dia);
        }

        // formatação de data no padrão brasileiro
        public String formatar(){
            String dataFormatada = "%d/%d/%d";
            return String.format(dataFormatada, getDia(), getMes(), getAno());
        }
    }

    public class Hora{
        private int hora;
        private int minuto;

        public Hora(int hora, int minuto){
            this.hora = hora;
            this.minuto = minuto;
        }

        // acesso aos atributos da Hora
        public int getHora() {
            return hora;
        }
        public int getMinuto() {
            return minuto;
        }

        // julgo não ser necessário implementação de stters por enquanto

        // processamento do tipo Hora
        public static Hora parseHora(String s){
            // s no formato HH:mm
            int hora = Integer.parseInt(s.substring(0,2));
            int minuto = Integer.parseInt(s.substring(4,5));
            return new Hora(hora, minuto);
        }

        // formatar Hora
        public String formatar(){
            String horaFormatada = "%d:%d";
            return String.format(horaFormatada, getHora(), getMinuto());
        }

    }

    // método principal
    public static void main(String[] args){

    }
}