    public void bisectar(int i, double x1, double x2, int count) {
        myp.parseExpression(fx.get(i));
        myp.setVarValue("x", x1);
        double fx1 = myp.getValue();
        myp.setVarValue("x", x2);
        double fx2 = myp.getValue();
        pr.println("Bisecci�n " + count);
        pr.println("Intervalo: [" + x1 + "," + x2 + "]");
        pr.println("f(x1) = " + fx1);
        pr.println("f(x2) = " + fx2);
        if (fx1 * fx2 < 0) {
            double xm = (x1 + x2) / 2;
            myp.parseExpression(fx.get(i));
            myp.addVariable("x", xm);
            double fxm = myp.getValue();
            pr.println("  xm  = " + xm);
            pr.println("f(xm) = " + fxm);
            if (fxm <= err.get(i) || count == max.get(i)) {
                pr.println("\nRA�Z ENCONTRADA: " + xm + "\nMARGEN DE ERROR: " + fxm + "\n");
            } else {
                pr.println();
                if (fx1 * fxm < 0) {
                    bisectar(i, x1, xm, ++count);
                } else {
                    if (fx2 * fxm < 0) {
                        bisectar(i, xm, x2, ++count);
                    } else {
                        pr.println("\nERROR: Intervalo inadecuado.");
                    }
                }
            }
        } else {
            pr.println("\nERROR: Intervalo inadecuado.");
        }
    }
