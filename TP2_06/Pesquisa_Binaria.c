#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h> // para arquivo log

/* ====== MODELAGEM ====== */

// === MINHAS FUNÇÕES ===
    int tamanho(char* s){
        int i=0;
        for(i; s[i]!='\0'; i++);
        return i;
    } //simula strlen()

    void copiar(char* destino, char* origem){
        int tamO = tamanho(origem);
        for(int i=0; i<=tamO; i++){
            destino[i] = origem[i];
        }
    } //simula strcpy()

    void concatenar(char* destino, char* origem) {
        int f = tamanho(destino);
        int j = 0;
        while (origem[j] != '\0') {
            destino[f] = origem[j];
            f++;
            j++;
        }
        destino[f] = '\0';
    }//simula strcat()
// ===

// === TIPO HORA ===
    typedef struct {
        int hora;
        int minuto;
    } Hora;

    Hora parse_hora(char* s);
    void formatar_hora(Hora* hora, char* buffer);
// ===

// === TIPO DATA ===
    typedef struct {
        int ano;
        int mes;
        int dia;
    } Data;

    Data parse_data(char* s);
    void formatar_data(Data* data, char* buffer);
// ===

// === TIPO RESTAURANTE === 
    typedef struct {
        int id;
        char* nome;
        char* cidade;
        int capacidade;
        double avaliacao;
        int n_tipos_cozinha;
        char** tipos_cozinha;
        int faixa_preco;
        Hora horario_abertura;
        Hora horario_fechamento;
        Data data_abertura;
        bool aberto;
    } Restaurante;

    Restaurante* parse_restaurante(char* s);
        void p_id_rest(Restaurante* rest, char* s);
        void p_nome_rest(Restaurante* rest, char* s);
        void p_cidade_rest(Restaurante* rest, char* s);
        void p_capacidade_rest(Restaurante* rest, char* s);
        void p_avaliacao_rest(Restaurante* rest, char* s);
        void p_t_cozinha_rest(Restaurante* rest, char* s);
        void p_f_preco_rest(Restaurante* rest, char* s);
        void p_horarios_rest(Restaurante* rest, char* s);
        void p_data_rest(Restaurante* rest, char* s);
        void p_aberto_rest(Restaurante* rest, char* s);
    // ===
    void formatar_restaurante(Restaurante* r, char* buffer);
// ===

// === TIPO COLEÇÃO DE RESTAURANTES ===
    typedef struct {
        int tamanho;
        int capacidade;
        Restaurante** restaurantes;
    } Colecao_Restaurantes;

    void inserir_no_fim(Colecao_Restaurantes* col, Restaurante* rest);
    Restaurante* buscar_por_id(Colecao_Restaurantes* col, int id);
    void ler_csv_colecao(Colecao_Restaurantes* col, char* path);
    Colecao_Restaurantes* ler_csv();
// ===

// === === ESTRUTURA DOS MÉTODOS === ===

// === MÉTODOS DE HORA ===
    Hora parse_hora(char* s) {
        Hora h;
        sscanf(s, "%d:%d", &h.hora, &h.minuto);
        return h;
    }

    void formatar_hora(Hora* hora, char* buffer) {
        sprintf(buffer, "%02d:%02d", hora->hora, hora->minuto);
    }
// ===

// === MÉTODOS DE DATA ===
    Data parse_data(char* s) {
        Data d;
        sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
        return d;
    }

    void formatar_data(Data* data, char* buffer) {
        sprintf(buffer, "%02d/%02d/%04d", data->dia, data->mes, data->ano);
    }
// ===

// === MÉTODOS DE RESTAURANTE ===

    Restaurante* parse_restaurante(char* s) {

        Restaurante* rest = (Restaurante*) malloc(sizeof(Restaurante));
        if (!rest) return NULL;

        char campos[10][300];
        int ln = 0, c = 0;

        for (int i = 0; s[i] != '\0' && s[i] != '\n' && s[i] != '\r'; i++) {
            if (s[i] == ',' && ln < 9) {
                campos[ln][c] = '\0';
                ln++;
                c = 0;
            } else {
                campos[ln][c++] = s[i];
            }
        }
        campos[ln][c] = '\0';

        p_id_rest(rest, campos[0]);
        p_nome_rest(rest, campos[1]);
        p_cidade_rest(rest, campos[2]);
        p_capacidade_rest(rest, campos[3]);
        p_avaliacao_rest(rest, campos[4]);
        p_t_cozinha_rest(rest, campos[5]);
        p_f_preco_rest(rest, campos[6]);
        p_horarios_rest(rest, campos[7]);
        p_data_rest(rest, campos[8]);
        p_aberto_rest(rest, campos[9]);

        return rest;
    }
    
    void p_id_rest(Restaurante* rest, char* s){
        sscanf(s, "%d", &(rest->id));
    }

    void p_nome_rest(Restaurante* rest, char* s){
        int tam = tamanho(s);
        char* nome = (char*)malloc((tam+1) * sizeof(char)); // espaço para \0
        if(nome == NULL) return;
        copiar(nome, s);
        rest->nome = nome;
    }

    void p_cidade_rest(Restaurante* rest, char* s){
        int tam = tamanho(s);
        char* cidade = (char*)malloc((tam+1) * sizeof(char));
        if(cidade == NULL) return;
        copiar(cidade, s);
        rest->cidade = cidade;
    }

    void p_capacidade_rest(Restaurante* rest, char* s){
        sscanf(s, "%d", &(rest->capacidade));
    }

    void p_avaliacao_rest(Restaurante* rest, char* s){
        sscanf(s, "%lf", &(rest->avaliacao));
    }

    void p_t_cozinha_rest(Restaurante* rest, char* s) {
        int seps = 0;
        
        for(int i = 0; s[i] != '\0'; i++) {
            if(s[i] == ';') {
                s[i] = '\0';
                seps++;
            }
        }
        int n_tipos = seps + 1;

        char** tp = (char**)malloc(n_tipos * sizeof(char*));
        if(tp == NULL) return;

        int ref = 0;
        for(int i = 0; i < n_tipos; i++) {
            char* substring = &(s[ref]);
            int tam = tamanho(substring);

            tp[i] = (char*)malloc((tam + 1) * sizeof(char));
            
            if(tp[i] != NULL) {
                copiar(tp[i], substring);
            }

            ref += (tam + 1); 
        }

        rest->n_tipos_cozinha = n_tipos;
        rest->tipos_cozinha = tp;
    }

    void p_f_preco_rest(Restaurante* rest, char* s){
        rest->faixa_preco = tamanho(s);
    }

    void p_horarios_rest(Restaurante* rest, char* s){
        char ha[8], hf[8];
        sscanf(s, "%[^-]-%s", ha, hf);
        rest->horario_abertura = parse_hora(ha);
        rest->horario_fechamento = parse_hora(hf);
    }

    void p_data_rest(Restaurante* rest, char* s){
        char d[11];
        sscanf(s, "%s", d);
        rest->data_abertura = parse_data(d);
    }

    void p_aberto_rest(Restaurante* rest, char* s){
        rest->aberto = (strcmp(s, "true") == 0);
    }

    void formatar_restaurante(Restaurante* r, char* buffer) {
        int tam = 64 * r->n_tipos_cozinha;
        char* coz = (char*)malloc(tam * sizeof(char));

        if (coz != NULL) {
            coz[0] = '\0';
        }

        for (int i = 0; i < r->n_tipos_cozinha; i++) {
            concatenar(coz, r->tipos_cozinha[i]);
            if (i + 1 < r->n_tipos_cozinha) concatenar(coz, ",");
        }

        char preco[8] = "";
        for (int i = 0; i < r->faixa_preco; i++) preco[i] = '$';
        preco[r->faixa_preco] = '\0';

        char ha[8], hf[8], data[16];
        formatar_hora(&r->horario_abertura, ha);
        formatar_hora(&r->horario_fechamento, hf);
        formatar_data(&r->data_abertura, data);

        sprintf(buffer,
            "[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %s-%s ## %s ## %s]",
            r->id, r->nome, r->cidade, r->capacidade, r->avaliacao,
            coz, preco, ha, hf, data,
            r->aberto ? "true" : "false");
    }
// ===

// === MÉTODO DE COLEÇÃO DE RESTAURANTES ===

    void inserir_no_fim(Colecao_Restaurantes* col, Restaurante* rest) {
        if (col->tamanho >= col->capacidade) {
            col->capacidade *= 2;
            col->restaurantes = (Restaurante**) realloc(col->restaurantes, col->capacidade * sizeof(Restaurante*));
        }
        col->restaurantes[col->tamanho++] = rest;
    }

    Restaurante* buscar_por_id(Colecao_Restaurantes* col, int id) {
        for (int i = 0; i < col->tamanho; i++) {
            if (col->restaurantes[i]->id == id) return col->restaurantes[i];
        }
        return NULL;
    }

    void ler_csv_colecao(Colecao_Restaurantes* col, char* path) {
        FILE* fp = fopen(path, "r");
        if (fp == NULL) {
            printf("Erro ao abrir arquivo %s\n", path);
            return;
        }
        char linha[1024];
        if (fgets(linha, sizeof(linha), fp) == NULL) { fclose(fp); return; }
        while (fgets(linha, sizeof(linha), fp) != NULL) {
            Restaurante* r = parse_restaurante(linha);
            inserir_no_fim(col, r);
        }
        fclose(fp);
    }

    Colecao_Restaurantes* ler_csv() {
        Colecao_Restaurantes* col = (Colecao_Restaurantes*) malloc(sizeof(Colecao_Restaurantes));
        col->tamanho = 0;
        col->capacidade = 500;
        col->restaurantes = (Restaurante**) malloc(col->capacidade * sizeof(Restaurante*));
        ler_csv_colecao(col, "/tmp/restaurantes.csv");
        return col;
    }
// ===

// === === ===

/* ====== ORDENACAO POR SELECAO ====== */
void swap(Restaurante** a, Restaurante** b) {
    Restaurante* tmp = *a;
    *a = *b;
    *b = tmp;
}

void selecao(Restaurante** array, int n) {
    for (int i = 0; i < n - 1; i++) {
        int menor = i;
        for (int j = i + 1; j < n; j++) {
            if (strcmp(array[menor]->nome, array[j]->nome) > 0) menor = j;
        }
        if (menor != i){
            swap(&array[menor], &array[i]);
        }
    }
}

/* ====== PESQUISA BINÁRIA ====== */
bool pesquisa_binaria(Restaurante** array, int n, char* chave, long* n_comp) {
    int esq = 0, dir = n - 1;
    while (esq <= dir) {
        int meio = (esq + dir) / 2;
        (*n_comp)++;
        int cmp = strcmp(array[meio]->nome, chave);
        if (cmp == 0) return true;
        if (cmp < 0) esq = meio + 1; else dir = meio - 1;
    }
    return false;
}

/* ====== PRINCIPAL ====== */
int main() {
    //1. montar array de restaurantes por id e depois ordená-los com seleção
    Colecao_Restaurantes* col = ler_csv();
    long comp = 0; 

    Restaurante** array = (Restaurante**) malloc(col->tamanho * sizeof(Restaurante*));
    int n_rest = 0;
    int id;
    
    while (scanf("%d", &id) == 1 && id != -1) {
        Restaurante* rest = buscar_por_id(col, id);
        if (rest) array[n_rest++] = rest;
    }
    int c;
    while ((c = getchar()) != '\n' && c != EOF);
    //limpando os resíduos deixados pelo scanf.

    selecao(array, n_rest);

    /** pesquisa binária:
     * 1. ler até encontrar "FIM";
     * 2. imprimir "sim" ou "não";
     * 3. medir tempo de pesquisa;
     * 4. contar comparações.
     */ 

    clock_t ini = clock();
    char nome[128];
    while (fgets(nome, sizeof(nome), stdin) != NULL) {
        nome[strcspn(nome, "\r\n")] = '\0';// substitui \r ou \n por \0
        if (strcmp(nome, "FIM") == 0) break;
        printf("%s\n", pesquisa_binaria(array, n_rest, nome, &comp) ? "SIM" : "NAO");
    }
    clock_t fim = clock();
    double tempo = (double)(fim - ini) / CLOCKS_PER_SEC;

    FILE* log = fopen("898910_binaria.txt", "w");
    if (log) {
        fprintf(log, "%s\t%ld\t%lf\n", "898910", comp, tempo);
        fclose(log);
    }

    // == LIBERANDO MEMÓRIA ==
    for (int i = 0; i < col->tamanho; i++){
        for(int j = 0; j < col->restaurantes[i]->n_tipos_cozinha; j++){
            free(col->restaurantes[i]->tipos_cozinha[j]);
        }
        free(col->restaurantes[i]->nome);
        free(col->restaurantes[i]->cidade);
        free(col->restaurantes[i]);
    }
    free(col->restaurantes);
    free(col);
    free(array);
    return 0;
}