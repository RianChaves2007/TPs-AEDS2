/**
exercicio 2. Modelagem em C:
Fazer em C o mesmo que foi feito em TP2_01\ColecaoRestaurantes.java.
 */

#include <stdio.h>

//meus "métodos"

Hora parse_hora(char* s){
    // recebe HH:mm
    // retorna instância de Hora
}

void formatar_hora(Hora* hora, char* buffer){
    // recebe pointer para hora
    // escreve no buffer a string de hora no formato HH:mm
    // usar sprintf
}

Data parse_data(char* s){
    // recebe YYYY-MM-DD
    // retorna instância de Data
}

void formatar_data(Data* data, char* buffer){
    // recebe pointer para data
    // escreve no buffer a string de data no formato DD/MM/YYYY
    // usar sprintf
}

Restaurante* parse_restaurante(char* s){
    // recebe uma linha de restaurante do dataset do arquivo csv
    // retorna um pointer para nova instância de Restaurante
}

void formatar_restaurante(Restaurante* restaurante, char* buffer){
    // recebe pointer para restaurante
    // escreve no buffer a string que representa o restaurante no formato [id ## nome ## cidade ## capacidade ## avaliacao ## [tipos cozinha] ## faixa preco ## horario abertura-horario fechamento ## data abertura ## aberto]
    // retorno no mesmo formato feito em TP2_01\ColecaoRestaurantes.java
}

void ler_csv_colecao(Colecao_Restaurantes* colecao, char* path){
    // lê arquivo CSV, cria os restaurantes e configura a coleção
}

Colecao_Restaurantes* ler_csv(){
    //lê o dataset do CSV e retorna a coleção (ponteiro) com os restaurantes
    // verificar necessidade de **
}

// minhas "classes"

typedef struct{
    int hora, minuto;
} Hora;

typedef struct{
    int ano, mes, dia;
} Data;

typedef struct{
    int id;
    char* nome;
    int capacidade;
    double avaliacao;
    int n_tipos_cozinha;
    char** n_tipos_cozinha;
    int faixa_preco;
    Hora horario_abertura;
    Hora horario_fechamento;
    Data data_abertura;
    bool aberto;
} Restaurante;

typedef struct{
    int tamanho;
    Restaurante** restaurante;
} Colecao_Restaurantes;

int main(){
    // instanciar a "classe" Colecao_Restaurantes
    // ler entrada de int (referência: pub.in) até encontar -1
    // imprimir restaurante formatado correspondente ao id da entrada
    // testar saida: pub.out
}