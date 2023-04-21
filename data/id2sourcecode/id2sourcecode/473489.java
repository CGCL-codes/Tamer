    public static void main(String[] args) throws IOException {
        int altura;
        int linea;
        int ast;
        InputStreamReader teclado;
        BufferedReader bufferLectura;
        teclado = new InputStreamReader(System.in);
        bufferLectura = new BufferedReader(teclado);
        do {
            System.out.print("\tIntroduzca la altura IMPAR de la figura: ");
            altura = Integer.parseInt(bufferLectura.readLine());
            System.out.println();
        } while (altura % 2 == 0);
        for (linea = 0; linea <= ((altura - 1) / 2); linea++) {
            for (ast = 0; ast <= linea; ast++) System.out.print("* ");
            System.out.println();
        }
        for (linea = altura / 2; linea > 0; linea--) {
            for (ast = linea; ast > 0; ast--) System.out.print("* ");
            System.out.println();
        }
    }
