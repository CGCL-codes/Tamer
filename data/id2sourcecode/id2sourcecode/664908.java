    public static String operacion(String entrada) {
        final int operadores_totales = 100;
        final int numeros_totales = 100;
        int i, j, k;
        int n_op = 0;
        int n_num = 0;
        float numero[] = new float[numeros_totales];
        char op[] = new char[operadores_totales];
        char entradaC[];
        String[] numeroS = new String[numeros_totales];
        String resultado;
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
        resultado = "" + (int) numero[0];
        return resultado;
    }
