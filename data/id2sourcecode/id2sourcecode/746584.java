    private static int[] getVizinhos(int id) {
        int[] vizinhosTemp = new int[3];
        int j = 0;
        for (int i = 0; i < distancias.length; i += 3) {
            if (id == distancias[i]) {
                vizinhosTemp[j] = distancias[i + 1];
                j++;
            } else if (id == distancias[i + 1]) {
                vizinhosTemp[j] = distancias[i];
                j++;
            }
        }
        int[] vizinhos = new int[j];
        for (int i = 0; i < j; i++) {
            vizinhos[i] = vizinhosTemp[i];
        }
        return vizinhos;
    }
