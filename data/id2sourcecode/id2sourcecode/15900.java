    public static void main(String[] args) throws IOException {
        int linea, altura, j, k;
        InputStreamReader teclado;
        BufferedReader bufferLectura;
        teclado = new InputStreamReader(System.in);
        bufferLectura = new BufferedReader(teclado);
        do {
            System.out.print("\tIntroduzca la altura IMPAR de la figura: ");
            altura = Integer.parseInt(bufferLectura.readLine());
            System.out.println();
        } while (altura % 2 == 0);
        for (linea = 1; linea <= altura; linea++) {
            for (j = 1; j <= linea; j++) System.out.print(' ');
            for (k = altura; k >= linea; k--) {
                System.out.print('*');
                if (k != linea) {
                    if (linea % 2 != 0) System.out.print('+'); else System.out.print('-');
                }
            }
            System.out.println();
        }
    }
