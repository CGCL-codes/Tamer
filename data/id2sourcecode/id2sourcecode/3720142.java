    public static void Interpreta(AreaInstrucoes AI, AreaLiterais AL) {
        topo = 0;
        b = 0;
        p = 0;
        S[1] = 0;
        S[2] = 0;
        S[3] = 0;
        operador = 0;
        String leitura;
        while (operador != 26) {
            operador = AI.AI[p].codigo;
            l = AI.AI[p].op1;
            a = AI.AI[p].op2;
            p = p + 1;
            switch(operador) {
                case 1:
                    p = S[b + 2];
                    topo = b - a;
                    b = S[b + 1];
                    break;
                case 2:
                    topo = topo + 1;
                    S[topo] = S[Base() + a];
                    break;
                case 3:
                    topo = topo + 1;
                    S[topo] = a;
                    break;
                case 4:
                    S[Base() + a] = S[topo];
                    topo = topo - 1;
                    break;
                case 5:
                    S[topo - 1] = S[topo - 1] + S[topo];
                    topo = topo - 1;
                    break;
                case 6:
                    S[topo - 1] = S[topo - 1] - S[topo];
                    topo = topo - 1;
                    break;
                case 7:
                    S[topo - 1] = S[topo - 1] * S[topo];
                    topo = topo - 1;
                    break;
                case 8:
                    if (S[topo] == 0) {
                        JOptionPane.showMessageDialog(null, "Divis�o por zero.", "Erro durante a execu��o", JOptionPane.ERROR_MESSAGE);
                    } else {
                        S[topo - 1] = S[topo - 1] / S[topo];
                        topo = topo - 1;
                    }
                    break;
                case 9:
                    S[topo] = -S[topo];
                    break;
                case 10:
                    S[topo] = 1 - S[topo];
                    break;
                case 11:
                    if ((S[topo - 1] == 1) && (S[topo] == 1)) {
                        S[topo - 1] = 1;
                    } else {
                        S[topo - 1] = 0;
                        topo = topo - 1;
                    }
                    break;
                case 12:
                    if ((S[topo - 1] == 1 || S[topo] == 1)) {
                        S[topo - 1] = 1;
                    } else {
                        S[topo - 1] = 0;
                        topo = topo - 1;
                    }
                    break;
                case 13:
                    if (S[topo - 1] < S[topo]) {
                        S[topo - 1] = 1;
                    } else {
                        S[topo - 1] = 0;
                    }
                    topo = topo - 1;
                    break;
                case 14:
                    if (S[topo - 1] > S[topo]) {
                        S[topo - 1] = 1;
                    } else {
                        S[topo - 1] = 0;
                    }
                    topo = topo - 1;
                    break;
                case 15:
                    if (S[topo - 1] == S[topo]) {
                        S[topo - 1] = 1;
                    } else {
                        S[topo - 1] = 0;
                    }
                    topo = topo - 1;
                    break;
                case 16:
                    if (S[topo - 1] != S[topo]) {
                        S[topo - 1] = 1;
                    } else {
                        S[topo - 1] = 0;
                    }
                    topo = topo - 1;
                    break;
                case 17:
                    if (S[topo - 1] <= S[topo]) {
                        S[topo - 1] = 1;
                    } else {
                        S[topo - 1] = 0;
                    }
                    topo = topo - 1;
                    break;
                case 18:
                    if (S[topo - 1] >= S[topo]) {
                        S[topo - 1] = 1;
                    } else {
                        S[topo - 1] = 0;
                    }
                    topo = topo - 1;
                    break;
                case 19:
                    p = a;
                    break;
                case 20:
                    if (S[topo] == 0) {
                        p = a;
                    }
                    topo = topo - 1;
                    break;
                case 21:
                    topo = topo + 1;
                    leitura = JOptionPane.showInputDialog(null, "Informe o valor:", "Leitura", JOptionPane.QUESTION_MESSAGE);
                    (S[topo]) = Integer.parseInt(leitura);
                    break;
                case 22:
                    JOptionPane.showMessageDialog(null, "" + S[topo], "Informa��o", JOptionPane.INFORMATION_MESSAGE);
                    topo = topo - 1;
                    break;
                case 23:
                    if (a >= AL.LIT) {
                        JOptionPane.showMessageDialog(null, "Literal n�o encontrado na �rea dos literais.", "Erro durante a execu��o", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "" + AL.AL[a], "Informa��o", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                case 24:
                    topo = topo + a;
                    break;
                case 25:
                    S[topo + 1] = Base();
                    S[topo + 2] = b;
                    S[topo + 3] = p;
                    b = topo + 1;
                    p = a;
                    break;
                case 26:
                    break;
                case 27:
                    break;
                case 28:
                    topo = topo + 1;
                    S[topo] = S[topo - 1];
                    break;
                case 29:
                    if (S[topo] == 1) {
                        p = a;
                    }
                    topo = topo - 1;
            }
        }
    }
