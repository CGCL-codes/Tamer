    public static void main(String[] args) throws IOException {
        final int caracteres = 100;
        int i, j, k, contador, acumulado = 0;
        int contadorV = 0, contadorL = 0, contadorD = 0;
        char entradaC[];
        boolean romano = false;
        int valores[] = new int[caracteres];
        String entrada;
        InputStreamReader teclado;
        BufferedReader bufferLectura;
        teclado = new InputStreamReader(System.in);
        bufferLectura = new BufferedReader(teclado);
        do {
            System.out.print("Introduzca el Número Romano: ");
            entrada = bufferLectura.readLine().toUpperCase();
            contador = 0;
            entradaC = new char[entrada.length()];
            for (i = 0; i < entrada.length(); i++) {
                entradaC[i] = entrada.charAt(i);
                if (entradaC[i] == 'I' || entradaC[i] == 'V' || entradaC[i] == 'X' || entradaC[i] == 'L' || entradaC[i] == 'C' || entradaC[i] == 'D' || entradaC[i] == 'M') {
                    contador++;
                }
                if (contador == entrada.length()) romano = true;
            }
            for (i = 3; i < entrada.length(); i++) {
                if (entradaC[i] == entradaC[i - 1] && entradaC[i - 1] == entradaC[i - 2] && entradaC[i - 2] == entradaC[i - 3]) {
                    romano = false;
                    System.out.println("\tERORR: No puedes poner más de 3 Símbolos iguales seguidos");
                }
            }
            for (i = 0; i < entrada.length(); i++) {
                if (entradaC[i] == 'V') contadorV++;
                if (entradaC[i] == 'L') contadorL++;
                if (entradaC[i] == 'D') contadorD++;
            }
            if (contadorV <= 1 || contadorL <= 1 || contadorD <= 1) ; else if (contadorV > 1 || contadorL > 1 || contadorD > 1) {
                romano = false;
                System.out.println("\tERROR: Aparece más de una vez V ó L ó D");
                contadorV = 0;
                contadorL = 0;
                contadorD = 0;
            }
        } while (romano != true);
        for (i = 0; i < contador; i++) {
            if (entradaC[i] == 'I') valores[i] = 1; else if (entradaC[i] == 'V') valores[i] = 5; else if (entradaC[i] == 'X') valores[i] = 10; else if (entradaC[i] == 'C') valores[i] = 100; else if (entradaC[i] == 'L') valores[i] = 50; else if (entradaC[i] == 'D') valores[i] = 500; else if (entradaC[i] == 'M') valores[i] = 1000;
        }
        for (i = 0; i <= contador; ) {
            if (valores[i] < valores[i + 1]) {
                valores[i + 1] = valores[i + 1] - valores[i];
                valores[i] = 0;
            } else if (valores[i] == valores[i + 1] && valores[i] != 0) {
                valores[i + 1] = valores[i + 1] + valores[i];
                valores[i] = 0;
            } else if (valores[i] == 0) i++; else i++;
            for (k = 0; k <= contador; k++) for (j = 0; j <= contador; j++) {
                if (valores[j] == 0) {
                    valores[j] = valores[j + 1];
                    valores[j + 1] = 0;
                }
            }
        }
        for (i = 0; i <= contador; i++) {
            acumulado += valores[i];
        }
        System.out.println();
        System.out.print("El Número Romano ");
        for (i = 0; i < contador; i++) System.out.print(entradaC[i]);
        System.out.println();
        System.out.print("Tiene un valor de " + acumulado);
    }
