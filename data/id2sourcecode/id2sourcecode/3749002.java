    public int localizarToken(int base, String chave) {
        int start = INDICES_PALAVRAS_RESERVADAS[base];
        int end = INDICES_PALAVRAS_RESERVADAS[base + 1] - 1;
        chave = chave.toUpperCase();
        while (start <= end) {
            int half = (start + end) / 2;
            int comp = PALAVRAS_RESERVADAS[half].compareTo(chave);
            if (comp == 0) return CODIGO_PALAVRAS_RESERVADAS[half]; else if (comp < 0) start = half + 1; else end = half - 1;
        }
        return base;
    }
