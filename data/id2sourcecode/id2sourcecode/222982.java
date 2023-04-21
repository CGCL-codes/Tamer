    public void removerLinha(int row) {
        for (int i = row; i < 1023; i++) {
            memoria[i] = memoria[i + 1];
            instrucao[i] = instrucao[i + 1];
        }
        memoria[1023] = 0;
        instrucao[1023] = "";
    }
