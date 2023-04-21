    public static int[] rotar(int[] numeros) {
        int primero = numeros[0];
        int x;
        for (x = 0; x < numeros.length - 1; x++) numeros[x] = numeros[x + 1];
        numeros[x] = primero;
        return numeros;
    }
