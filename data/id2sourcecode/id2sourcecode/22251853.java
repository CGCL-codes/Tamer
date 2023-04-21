    private void btnIntegrationComputeActionPerformed(java.awt.event.ActionEvent evt) {
        Frazione a = calcolaEspressione(txtIntegrationFrom.getText());
        txtIntegrationFrom.setText("" + a);
        Frazione b = calcolaEspressione(txtIntegrationTo.getText());
        txtIntegrationTo.setText("" + b);
        Frazione result = new Frazione(0);
        long nanoTime = 0l;
        double eps = Math.pow(10.0, -12.0);
        switch(comboAlgorithm.getSelectedIndex()) {
            case 0:
                metodoSimpson: {
                    appendToBuffer("Integrating (Metodo Cavalieri-Simpson) <a href=\"#copyFormula:" + f() + "\">" + f() + "</a>");
                    nanoTime = System.nanoTime();
                    Frazione h = b.subtract(a).divideBy(2);
                    Frazione s1 = f(a).add(f(b));
                    Frazione s2 = new Frazione(0.0);
                    Frazione s4 = f(a.add(h));
                    int n = 2;
                    Frazione sNuovo = h.multiplyBy(s1.multiplyBy(s4.multiplyBy(4))).divideBy(3);
                    Frazione sVecchio = sNuovo;
                    do {
                        sVecchio = sNuovo;
                        n = 2 * n;
                        h = h.divideBy(2);
                        s2 = s2.add(s4);
                        s4 = new Frazione(0);
                        int j = 1;
                        do {
                            s4 = s4.add(f(a.add(h.multiplyBy(j))));
                            j = j + 2;
                        } while (j <= n);
                        sNuovo = h.multiplyBy(s1.add(s2.multiplyBy(2).add(s4.multiplyBy(4)))).divideBy(3);
                    } while (FMath.abs(sNuovo.subtract(sVecchio)).doubleValue() > eps);
                    nanoTime = System.nanoTime() - nanoTime;
                    result = sNuovo;
                }
                break;
            case 1:
                appendToBuffer("Integrating (Regola punto medio) <a href=\"#copyFormula:" + f() + "\">" + f() + "</a>");
                break;
            case 2:
                appendToBuffer("Integrating (Metodo romberg) <a href=\"#copyFormula:" + f() + "\">" + f() + "</a>");
                nanoTime = System.nanoTime();
                metodoRomberg: {
                    int precisione = Integer.parseInt(txtIntegrationN.getText());
                    Frazione[] t = new Frazione[precisione];
                    Frazione h = b.subtract(a);
                    Frazione s = f(a).add(f(b)).divideBy(2);
                    t[0] = s.multiplyBy(h);
                    int n = 1;
                    int i = 1;
                    Frazione vecchioValore = new Frazione(0);
                    Frazione vhj = new Frazione(0.0);
                    do {
                        n = n * 2;
                        h = h.divideBy(2);
                        int j = 1;
                        do {
                            s = s.add(f(a.add(h.multiplyBy(j))));
                            j = j + 2;
                        } while (j <= n);
                        t[i] = s.multiplyBy(h);
                        vhj = new Frazione(1);
                        for (int k = i - 1; k >= 0; k--) {
                            vhj = vhj.multiplyBy(4);
                            vecchioValore = t[k];
                            t[k] = t[k + 1].add(t[k + 1].subtract(vecchioValore).divideBy(vhj.subtract(1)));
                        }
                        i = i + 1;
                    } while (FMath.abs(vecchioValore.subtract(t[0])).doubleValue() > eps && i <= precisione - 1);
                    nanoTime = System.nanoTime() - nanoTime;
                    if (i > 15) {
                        JOptionPane.showMessageDialog(this, "Non è stata ottenuta la precisione voluta");
                    }
                    result = t[0];
                }
                break;
            case 3:
                {
                    metodoTrapezi: {
                        appendToBuffer("Integrating (Metodo trapezi) <a href=\"#copyFormula:" + f() + "\">" + f() + "</a>");
                        nanoTime = System.nanoTime();
                        int n = 1;
                        Frazione h = b.subtract(a);
                        Frazione T = h.multiplyBy(f(a).add(f(b))).divideBy(2);
                        Frazione M = new Frazione(0);
                        do {
                            M = new Frazione(0);
                            for (int i = 0; i < n - 1; i++) {
                                M = M.add(f(a.add(h.multiplyBy(i + 0.5d))));
                            }
                            M = M.multiplyBy(h);
                            T = T.add(M).divideBy(2);
                            h = h.divideBy(2);
                            n = 2 * n;
                        } while (FMath.abs(T.subtract(M)).doubleValue() > eps);
                        nanoTime = System.nanoTime() - nanoTime;
                        result = T;
                    }
                }
                break;
            case 4:
                {
                    appendToBuffer("Integrating (Metodo trapezi-b) <a href=\"#copyFormula:" + f() + "\">" + f() + "</a>");
                    nanoTime = System.nanoTime();
                    int n = 1;
                    Frazione h = b.subtract(a).divideBy(n);
                    Frazione somma = f(a);
                    for (int i = 1; i < n - 1; i++) {
                        somma = somma.add(f(a.add(h.multiplyBy(i))).multiplyBy(2));
                    }
                    somma = somma.add(f(b));
                    somma = somma.multiplyBy(h.divideBy(2));
                    nanoTime = System.nanoTime() - nanoTime;
                    result = somma;
                }
                break;
        }
        appendToBuffer("from a=" + a + " to b=" + b);
        int i = 0;
        final String[] unit = { "nanoseconds", "microseconds", "milliseconds", "seconds" };
        double dNanoTime = (double) nanoTime;
        while (dNanoTime > 1000 && i < 3) {
            dNanoTime /= 1000;
            i++;
        }
        appendToBuffer("=> <a href=\"#copyValue:" + result + "\">" + result + "</a> in ~" + round(dNanoTime, 2) + " " + unit[i]);
        txtFx.setText("" + result);
    }
