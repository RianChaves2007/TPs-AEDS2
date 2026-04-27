/*
 * TP2 - Exercicio 12
 * Pilha de registros (Restaurante) com alocacao sequencial em C.
 * Estrutura baseada na pilha de inteiros vista em sala de aula
 * (referencias_codigo/pilha.c), porem usando arranjo (sequencial).
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

/* ===== AUXILIARES DE STRING (sem strlen/strcpy/strcat) ===== */
int tamanho(char* s) {
    int i = 0;
    for (; s[i] != '\0'; i++);
    return i;
}

void copiar(char* destino, char* origem) {
    int t = tamanho(origem);
    for (int i = 0; i <= t; i++) destino[i] = origem[i];
}

void concatenar(char* destino, char* origem) {
    int f = tamanho(destino);
    int j = 0;
    while (origem[j] != '\0') {
        destino[f++] = origem[j++];
    }
    destino[f] = '\0';
}

/* ===== TIPO HORA ===== */
typedef struct {
    int hora;
    int minuto;
} Hora;

Hora parse_hora(char* s) {
    Hora h;
    sscanf(s, "%d:%d", &h.hora, &h.minuto);
    return h;
}

void formatar_hora(Hora* hora, char* buffer) {
    sprintf(buffer, "%02d:%02d", hora->hora, hora->minuto);
}

/* ===== TIPO DATA ===== */
typedef struct {
    int ano;
    int mes;
    int dia;
} Data;

Data parse_data(char* s) {
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}

void formatar_data(Data* data, char* buffer) {
    sprintf(buffer, "%02d/%02d/%04d", data->dia, data->mes, data->ano);
}

/* ===== TIPO RESTAURANTE ===== */
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

void p_id_rest(Restaurante* rest, char* s) {
    sscanf(s, "%d", &(rest->id));
}

void p_nome_rest(Restaurante* rest, char* s) {
    int t = tamanho(s);
    char* nome = (char*) malloc((t + 1) * sizeof(char));
    if (nome == NULL) return;
    copiar(nome, s);
    rest->nome = nome;
}

void p_cidade_rest(Restaurante* rest, char* s) {
    int t = tamanho(s);
    char* cidade = (char*) malloc((t + 1) * sizeof(char));
    if (cidade == NULL) return;
    copiar(cidade, s);
    rest->cidade = cidade;
}

void p_capacidade_rest(Restaurante* rest, char* s) {
    sscanf(s, "%d", &(rest->capacidade));
}

void p_avaliacao_rest(Restaurante* rest, char* s) {
    sscanf(s, "%lf", &(rest->avaliacao));
}

void p_t_cozinha_rest(Restaurante* rest, char* s) {
    int seps = 0;
    for (int i = 0; s[i] != '\0'; i++) {
        if (s[i] == ';') {
            s[i] = '\0';
            seps++;
        }
    }
    int n_tipos = seps + 1;
    char** tp = (char**) malloc(n_tipos * sizeof(char*));
    if (tp == NULL) return;
    int ref = 0;
    for (int i = 0; i < n_tipos; i++) {
        char* substring = &(s[ref]);
        int t = tamanho(substring);
        tp[i] = (char*) malloc((t + 1) * sizeof(char));
        if (tp[i] != NULL) copiar(tp[i], substring);
        ref += (t + 1);
    }
    rest->n_tipos_cozinha = n_tipos;
    rest->tipos_cozinha = tp;
}

void p_f_preco_rest(Restaurante* rest, char* s) {
    rest->faixa_preco = tamanho(s);
}

void p_horarios_rest(Restaurante* rest, char* s) {
    char ha[8], hf[8];
    sscanf(s, "%[^-]-%s", ha, hf);
    rest->horario_abertura = parse_hora(ha);
    rest->horario_fechamento = parse_hora(hf);
}

void p_data_rest(Restaurante* rest, char* s) {
    char d[16];
    sscanf(s, "%s", d);
    rest->data_abertura = parse_data(d);
}

void p_aberto_rest(Restaurante* rest, char* s) {
    rest->aberto = (strcmp(s, "true") == 0);
}

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

void formatar_restaurante(Restaurante* r, char* buffer) {
    int t = 64 * r->n_tipos_cozinha;
    char* coz = (char*) malloc(t * sizeof(char));
    if (coz != NULL) coz[0] = '\0';
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
    free(coz);
}

/* ===== COLECAO DE RESTAURANTES ===== */
typedef struct {
    int tamanho;
    int capacidade;
    Restaurante** restaurantes;
} Colecao_Restaurantes;

void inserir_no_fim(Colecao_Restaurantes* col, Restaurante* rest) {
    if (col->tamanho >= col->capacidade) {
        col->capacidade *= 2;
        col->restaurantes = (Restaurante**) realloc(col->restaurantes,
            col->capacidade * sizeof(Restaurante*));
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

/* ===== PILHA SEQUENCIAL DE RESTAURANTES ===== */
#define TAM_PILHA 1000

typedef struct {
    Restaurante* itens[TAM_PILHA];
    int topo;
} Pilha;

void pilha_iniciar(Pilha* p) {
    p->topo = 0;
}

void pilha_inserir(Pilha* p, Restaurante* r) {
    if (p->topo >= TAM_PILHA) {
        fprintf(stderr, "Erro ao inserir!\n");
        exit(1);
    }
    p->itens[p->topo++] = r;
}

Restaurante* pilha_remover(Pilha* p) {
    if (p->topo == 0) {
        fprintf(stderr, "Erro ao remover!\n");
        exit(1);
    }
    return p->itens[--p->topo];
}

void pilha_mostrar(Pilha* p) {
    char buffer[2048];
    for (int i = p->topo - 1; i >= 0; i--) {
        formatar_restaurante(p->itens[i], buffer);
        printf("%s\n", buffer);
    }
}

/* ===== PRINCIPAL ===== */
int main() {
    Colecao_Restaurantes* col = ler_csv();
    Pilha pilha;
    pilha_iniciar(&pilha);

    int id;
    /* Parte 1: ids ate -1, empilhados */
    while (scanf("%d", &id) == 1 && id != -1) {
        Restaurante* r = buscar_por_id(col, id);
        if (r != NULL) pilha_inserir(&pilha, r);
    }

    /* Parte 2: n comandos I/R */
    int n;
    if (scanf("%d", &n) != 1) return 0;
    char cmd[8];
    for (int k = 0; k < n; k++) {
        if (scanf("%s", cmd) != 1) break;
        if (strcmp(cmd, "I") == 0) {
            scanf("%d", &id);
            Restaurante* r = buscar_por_id(col, id);
            if (r != NULL) pilha_inserir(&pilha, r);
        } else if (strcmp(cmd, "R") == 0) {
            Restaurante* r = pilha_remover(&pilha);
            printf("(R)%s\n", r->nome);
        }
    }

    /* Saida final: pilha do topo para a base */
    pilha_mostrar(&pilha);
    return 0;
}
