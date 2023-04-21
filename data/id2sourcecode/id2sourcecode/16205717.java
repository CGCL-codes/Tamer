    public boolean removeDesarrollo(int d) {
        boolean encontrada = false;
        for (int i = 0; i < numDesarrollo; i++) {
            if (desarrollo[i] == d) encontrada = true;
            if (encontrada && i < numDesarrollo - 1) desarrollo[i] = desarrollo[i + 1];
        }
        if (encontrada) numDesarrollo--;
        return encontrada;
    }
