    private void ordenaPorNdGrupos(boolean inverso) {
        if (inverso) {
            for (int x = materias.length - 2; x >= 0; x--) for (int y = x; y <= materias.length - 2; y++) {
                if (materias[y].length < materias[y + 1].length) {
                    Grupo tmp[] = materias[y];
                    materias[y] = materias[y + 1];
                    materias[y + 1] = tmp;
                } else break;
            }
        } else {
            for (int x = materias.length - 2; x >= 0; x--) for (int y = x; y <= materias.length - 2; y++) {
                if (materias[y].length > materias[y + 1].length) {
                    Grupo tmp[] = materias[y];
                    materias[y] = materias[y + 1];
                    materias[y + 1] = tmp;
                } else break;
            }
        }
    }
