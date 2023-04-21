    public static void main(String[] args) {
        float uGrenze = 0;
        float oGrenze = 0;
        float ergebnis;
        int koeffizientenEinlesung = 0;
        int exponent = 0;
        boolean gleicheGrenzen = false;
        boolean eingabeEnde = false;
        boolean falscheEingabe = false;
        boolean sofortEnde = false;
        int[] koeffizientToExponent = new int[MAX_EXPONENTEN + 1];
        for (int i = 0; i <= MAX_EXPONENTEN; i++) {
            koeffizientToExponent[i] = 0;
        }
        try {
            float grenze1 = readFloat();
            float grenze2 = readFloat();
            if (grenze1 != grenze2) {
                oGrenze = Math.max(grenze1, grenze2);
                uGrenze = Math.min(grenze1, grenze2);
            } else {
                gleicheGrenzen = true;
            }
            koeffizientenEinlesung = readInt();
            if (koeffizientenEinlesung == ENDZEICHEN) {
                sofortEnde = true;
            } else {
                while (eingabeEnde == false) {
                    exponent = readInt();
                    if (exponent == ENDZEICHEN) {
                        eingabeEnde = true;
                        falscheEingabe = true;
                    } else {
                        if (exponent >= MIN_EXPONENTEN && exponent <= MAX_EXPONENTEN && koeffizientToExponent[exponent] == 0) {
                            koeffizientToExponent[exponent] = koeffizientenEinlesung;
                        } else {
                            falscheEingabe = true;
                        }
                    }
                    if (exponent != ENDZEICHEN) {
                        koeffizientenEinlesung = readInt();
                        if (koeffizientenEinlesung == ENDZEICHEN) {
                            eingabeEnde = true;
                        }
                    }
                }
            }
            if (falscheEingabe == true || sofortEnde == true) {
                EprogIO.println("FALSCHE EINGABE");
            } else {
                if (gleicheGrenzen == true) {
                    EprogIO.println("0");
                } else {
                    long n;
                    float grenzenDifferenz;
                    float xk;
                    float xk_minus_1;
                    float xk_zwischenWert;
                    float teilSumme;
                    float gesamtSumme = 0;
                    grenzenDifferenz = oGrenze - uGrenze;
                    if (grenzenDifferenz < TEILINTERVALLBREITE) {
                        n = 1;
                    } else {
                        n = Math.round(grenzenDifferenz / TEILINTERVALLBREITE);
                    }
                    for (int k = 1; k <= n; k++) {
                        xk_minus_1 = uGrenze + (((oGrenze - uGrenze) / n) * (k - 1));
                        xk = uGrenze + (((oGrenze - uGrenze) / n) * k);
                        xk_zwischenWert = (xk_minus_1 + xk) / 2;
                        teilSumme = 0;
                        for (int exp = MIN_EXPONENTEN; exp <= MAX_EXPONENTEN; exp++) {
                            double blub = exp + Math.pow(xk_zwischenWert, exp);
                            teilSumme += koeffizientToExponent[exp] * (Math.pow(xk_zwischenWert, exp));
                        }
                        gesamtSumme += teilSumme;
                    }
                    ergebnis = (oGrenze - uGrenze) / n * gesamtSumme;
                    printFixedln(ergebnis);
                }
            }
        } catch (EprogException e) {
            EprogIO.println("?");
        }
    }
