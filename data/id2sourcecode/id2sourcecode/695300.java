    public static void main(String[] args) throws IOException {
        int altura;
        int i, j;
        InputStreamReader teclado;
        BufferedReader bufferLectura;
        teclado = new InputStreamReader(System.in);
        bufferLectura = new BufferedReader(teclado);
        do {
            System.out.print("\tIntroduzca la altura IMPAR de la figura: ");
            altura = Integer.parseInt(bufferLectura.readLine());
            System.out.println();
        } while (altura % 2 == 0);
        for (i = 0; i <= altura / 2; i++) {
            for (j = altura / 2 - i; j > 0; j--) System.out.print("  ");
            System.out.print("* ");
            for (j = 1; j < 2 * i; j++) System.out.print("  ");
            if (i != 0) System.out.print("*");
            System.out.println();
        }
        for (i = altura / 2; i > 0; i--) {
            for (j = altura / 2 - i + 1; j > 0; j--) System.out.print("  ");
            System.out.print("* ");
            for (j = 2 * (i - 1) - 1; j > 0; j--) System.out.print("  ");
            if (i != 1) System.out.print("* ");
            System.out.println();
        }
    }
