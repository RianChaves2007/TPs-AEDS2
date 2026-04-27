#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>

/* === MODELAGEM === */

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

/* === */

/* ====== COUNTING SORT ====== */
int get_maior(Restaurante** array, int n, long* comp) {
    int maior = array[0]->capacidade;
    for (int i = 0; i < n; i++) {
        (*comp)++;
        if (array[i]->capacidade > maior) maior = array[i]->capacidade;
    }
    return maior;
}

void counting_sort(Restaurante** array, int n, long* comp, long* mov) {
    int tamCount = get_maior(array, n, comp) + 1;
    int* count = (int*)calloc(tamCount, sizeof(int));
    Restaurante** ordenado = (Restaurante**) malloc(n * sizeof(Restaurante*));

    for (int i = 0; i < n; i++) count[array[i]->capacidade]++;
    for (int i = 1; i < tamCount; i++) count[i] += count[i - 1];
    for (int i = n - 1; i >= 0; i--) {
        ordenado[count[array[i]->capacidade] - 1] = array[i];
        count[array[i]->capacidade]--;
        (*mov)++;
    }
    for (int i = 0; i < n; i++) { 
        array[i] = ordenado[i]; 
        (*mov)++; 
    }
    free(count); free(ordenado);
}

/* === PRINCICAL === */
int main() {
    long comp = 0;
    long mov = 0;
    Colecao_Restaurantes* col = ler_csv();
    Restaurante** array = (Restaurante**) malloc(500 * sizeof(Restaurante*));
    int n = 0;
    int id;
    while (scanf("%d", &id) == 1 && id != -1) {
        Restaurante* r = buscar_por_id(col, id);
        if (r) array[n++] = r;
    }

    clock_t ini = clock();
    if (n > 0) counting_sort(array, n, &comp, &mov);
    clock_t fim = clock();
    double tempo = (double)(fim - ini) / CLOCKS_PER_SEC;

    char buf[1024];
    for (int i = 0; i < n; i++) {
        formatar_restaurante(array[i], buf);
        printf("%s\n", buf);
    }

    FILE* log = fopen("898910_countingsort.txt", "w");
    if (log) {
        fprintf(log, "%s\t%ld\t%ld\t%.6f\n", "898910", comp, mov, tempo);
        fclose(log);
    }

    free(array);
    for (int i = 0; i < col->tamanho; i++) free(col->restaurantes[i]);
    free(col->restaurantes); free(col);
    return 0;
}
