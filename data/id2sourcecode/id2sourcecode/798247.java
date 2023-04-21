    public static boolean startTask(int[] array, int value, int v1, int v2) {
        int aux = (v1 + v2) / 2;
        if (array[aux] == value) {
            return true;
        } else if (aux == v1) {
            return false;
        }
        if (array[aux] > value) {
            return startTask(array, value, v1, aux);
        } else {
            return startTask(array, value, aux, v2);
        }
    }
