    public void atribuicaoAritmeticaVetor(String sentenca[]) {
        String rotuloVetorRecebe = sentenca[0];
        String posicaoVetor = sentenca[1];
        String expressao[] = new String[sentenca.length - 1];
        expressao[0] = new String(rotuloVetorRecebe + "-" + posicaoVetor);
        for (int i = 1; i < expressao.length; i++) {
            expressao[i] = sentenca[i + 1];
        }
        atribuicaoAritmetica(expressao);
    }
