    private int proximoEstado(char c, int state) {
        int inicio = INDICE_DE_ESTADOS[state];
        int fim = INDICE_DE_ESTADOS[state + 1] - 1;
        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            if (AUTOMATO_DE_ESTADOS[meio][0] == c) return AUTOMATO_DE_ESTADOS[meio][1]; else if (AUTOMATO_DE_ESTADOS[meio][0] < c) inicio = meio + 1; else fim = meio - 1;
        }
        return -1;
    }
