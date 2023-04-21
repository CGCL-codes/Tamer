    public static void main(String[] args) throws IOException {
        int i, j;
        int altura;
        char matriz[][];
        InputStreamReader teclado;
        BufferedReader bufferLectura;
        teclado = new InputStreamReader(System.in);
        bufferLectura = new BufferedReader(teclado);
        do {
            System.out.print("\tIntroduzca la altura IMPAR de la figura: ");
            altura = Integer.parseInt(bufferLectura.readLine());
            System.out.println();
        } while (altura % 2 == 0);
        matriz = new char[altura][altura];
        for (i = 0; i < altura; i++) for (j = 0; j < altura; j++) matriz[i][j] = ' ';
        for (i = (altura) / 2, j = 0; i >= 0 && j <= (altura) / 2; i--, j++) matriz[i][j] = '*';
        for (i = 0, j = altura / 2; i < altura && j < altura; i++, j++) matriz[i][j] = '*';
        for (i = altura / 2, j = 0; i < altura && j <= altura / 2; i++, j++) matriz[i][j] = '*';
        for (i = altura - 1, j = altura / 2; i >= altura / 2 && j < altura; i--, j++) matriz[i][j] = '*';
        for (i = 0; i < altura; i++) matriz[i][altura / 2] = '*';
        for (j = 0; j < altura; j++) matriz[altura / 2][j] = '*';
        for (i = 0; i < altura; i++) {
            for (j = 0; j < altura; j++) {
                System.out.print(matriz[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
