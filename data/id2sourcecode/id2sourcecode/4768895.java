    public static void mergeSort(int[] x, int[] xTemp, int esq, int dir) {
        if (esq < dir) {
            int medio = (esq + dir) / 2;
            mergeSort(x, xTemp, esq, medio);
            mergeSort(x, xTemp, medio + 1, dir);
            join(x, xTemp, esq, medio + 1, dir);
        }
    }
