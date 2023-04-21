    public static List<Integer> dividirEConquistar(int[] a, int esquerda, int direita) {
        if (esquerda == direita) {
            if (a[esquerda] > 0) {
                return Arrays.asList(a[esquerda], esquerda, direita);
            } else {
                return Arrays.asList(0, esquerda, direita);
            }
        }
        int meio = (esquerda + direita) / 2;
        int somaMaximaDaEsquerda = 0;
        int soma = 0;
        int i = -1;
        for (int k = meio; k >= esquerda; k--) {
            soma = soma + a[k];
            if (soma > somaMaximaDaEsquerda) {
                somaMaximaDaEsquerda = soma;
                i = k;
            }
        }
        int somaMaximaDaDireita = 0;
        soma = 0;
        int j = -1;
        for (int k = meio + 1; k <= direita; k++) {
            soma = soma + a[k];
            if (soma > somaMaximaDaDireita) {
                somaMaximaDaDireita = soma;
                j = k;
            }
        }
        List<Integer> resultadoDaSomaMaximaDoCentro = Arrays.asList(somaMaximaDaEsquerda + somaMaximaDaDireita, i, j);
        List<Integer> resultadoDaSomaMaximaDoPrefixo = dividirEConquistar(a, esquerda, meio);
        List<Integer> resultadoDaSomaMaximaDoSufixo = dividirEConquistar(a, meio + 1, direita);
        if (resultadoDaSomaMaximaDoPrefixo.get(0) >= resultadoDaSomaMaximaDoSufixo.get(0)) {
            if (resultadoDaSomaMaximaDoPrefixo.get(0) >= resultadoDaSomaMaximaDoCentro.get(0)) {
                return resultadoDaSomaMaximaDoPrefixo;
            } else {
                return resultadoDaSomaMaximaDoCentro;
            }
        } else {
            if (resultadoDaSomaMaximaDoSufixo.get(0) >= resultadoDaSomaMaximaDoCentro.get(0)) {
                return resultadoDaSomaMaximaDoSufixo;
            } else {
                return resultadoDaSomaMaximaDoCentro;
            }
        }
    }
