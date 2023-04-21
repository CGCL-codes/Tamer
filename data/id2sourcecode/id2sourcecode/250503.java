    public static void main(String[] args) throws IOException {
        final int operadores_totales = 10;
        final int numeros_totales = 10;
        int i, j, k;
        int n_op = 0;
        int n_num = 0;
        float numero[] = new float[numeros_totales];
        char op[] = new char[operadores_totales];
        char entradaC[];
        String[] numeroS = new String[numeros_totales];
        String entrada;
        InputStreamReader teclado;
        BufferedReader bufferLectura;
        teclado = new InputStreamReader(System.in);
        bufferLectura = new BufferedReader(teclado);
        System.out.println("\tOPERACIONES POSIBLES (Entre paréntesis su PREFERENCIA)\n- Suma:\t\t\t \"+\"(3)\n- Resta:\t\t \"-\"(3)\n- Multiplicación:\t \"*\"(2)\n- División:\t\t \"/\"(2)\n- Potencia:\t\t \"**\"(1) (después debe ponerse el exponente)\n\tEj. 3³ + 5 = 3**3+5");
        System.out.print("Introduce la operación: ");
        entrada = bufferLectura.readLine();
        entradaC = new char[entrada.length()];
        for (i = 0; i < entrada.length(); i++) entradaC[i] = entrada.charAt(i);
        for (i = 0; i < entrada.length(); i++) {
            if (entradaC[i] == '/' || entradaC[i] == '-' || entradaC[i] == '+') {
                op[n_op] = entradaC[i];
                n_op++;
            } else if (entradaC[i] == '*') {
                if (entradaC[i + 1] == '*') {
                    op[n_op] = '^';
                    n_op++;
                    i++;
                } else {
                    op[n_op] = '*';
                    n_op++;
                }
            }
        }
        for (i = 0; i <= 9; i++) numeroS[i] = "";
        for (i = 0; i < entrada.length(); i++) {
            if (entradaC[i] == '/' || entradaC[i] == '-' || entradaC[i] == '+') {
                n_num++;
            } else if (entradaC[i] == '*') {
                if (entradaC[i + 1] == '*' || entradaC[i] == '/' || entradaC[i] == '-' || entradaC[i] == '+') {
                    i++;
                    n_num++;
                } else n_num++;
            } else numeroS[n_num] += String.valueOf(entradaC[i]);
        }
        for (i = 0; i <= n_num; i++) {
            if (numeroS[i] != "") numero[i] = Float.parseFloat(numeroS[i]);
        }
        System.out.print("Operación Evaluada: ");
        for (i = 0, j = 0; i <= n_num; i++, j++) {
            System.out.print(numero[i]);
            System.out.print(op[j]);
        }
        System.out.println();
        for (i = 0, j = 0; i <= n_op; ) {
            if (op[i] == '^') {
                numero[i] = potencia(numero[i], numero[i + 1]);
                op[i] = '0';
                numero[i + 1] = 0;
            } else i++;
            for (k = 0; k <= n_num; k++) for (j = 0; j <= n_num; j++) {
                if (numero[j] == 0) {
                    numero[j] = numero[j + 1];
                    numero[j + 1] = 0;
                }
            }
            for (k = 0; k <= n_num; k++) for (j = 0; j <= n_num; j++) {
                if (op[j] == '0') {
                    op[j] = op[j + 1];
                    op[j + 1] = '0';
                }
            }
        }
        System.out.print("Operación tras realizar las POTENCIAS: ");
        for (i = 0, j = 0; i <= n_num; i++, j++) {
            System.out.print(numero[i]);
            System.out.print(op[j]);
        }
        System.out.println();
        for (i = 0; i <= n_op; ) {
            if (op[i] == '*') {
                numero[i] = numero[i] * numero[i + 1];
                op[i] = '0';
                numero[i + 1] = 0;
            } else if (op[i] == '/') {
                numero[i] = numero[i] / numero[i + 1];
                op[i] = '0';
                numero[i + 1] = 0;
            } else i++;
            for (k = 0; k <= n_num; k++) for (j = 0; j <= n_num; j++) {
                if (numero[j] == 0) {
                    numero[j] = numero[j + 1];
                    numero[j + 1] = 0;
                }
            }
            for (k = 0; k <= n_num; k++) for (j = 0; j <= n_num; j++) {
                if (op[j] == '0') {
                    op[j] = op[j + 1];
                    op[j + 1] = '0';
                }
            }
        }
        System.out.print("Operación tras realizar MULTIPLICACIONES Y DIVISIONES: ");
        for (i = 0, j = 0; i <= n_num; i++, j++) {
            System.out.print(numero[i]);
            System.out.print(op[j]);
        }
        System.out.println();
        for (i = 0; i <= n_op; i++) {
            if (op[0] == '+') {
                numero[0] = numero[0] + numero[1];
                op[0] = '0';
                numero[i + 1] = 0;
            }
            if (op[0] == '-') {
                numero[0] = numero[0] - numero[1];
                op[0] = '0';
                numero[1] = 0;
            }
            for (k = 0; k <= n_num; k++) for (j = 0; j <= n_num; j++) {
                if (numero[j] == 0) {
                    numero[j] = numero[j + 1];
                    numero[j + 1] = 0;
                }
            }
            for (k = 0; k <= n_num; k++) for (j = 0; j <= n_num; j++) {
                if (op[j] == '0') {
                    op[j] = op[j + 1];
                    op[j + 1] = '0';
                }
            }
        }
        System.out.println("RESULTADO: " + numero[0]);
    }
