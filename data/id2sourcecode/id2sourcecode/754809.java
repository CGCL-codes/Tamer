    public static String perform() {
        boolean eingabeende = false;
        boolean ende = false;
        boolean falscheeingabe = false;
        float summe_fvonx = 0;
        float abbildung_fvonxfuerk = 0;
        int[] koeff_zu_exponent = new int[hoechster_exponent + 1];
        for (int i = 0; i <= hoechster_exponent; i++) koeff_zu_exponent[i] = 0;
        try {
            float grenze1 = readFloat();
            float grenze2 = readFloat();
            float untere_grenze = Math.min(grenze1, grenze2);
            float obere_grenze = Math.max(grenze1, grenze2);
            if (untere_grenze == obere_grenze) {
                ende = true;
                liesZuende();
                return ("0.000");
            } else {
                do {
                    int gelesener_koeffizient = readInt();
                    if (gelesener_koeffizient == datensatzende) eingabeende = true; else {
                        int gelesener_exponent = readInt();
                        if ((gelesener_exponent < 0) || (gelesener_exponent > hoechster_exponent) || (koeff_zu_exponent[gelesener_exponent] != 0)) {
                            falscheeingabe = true;
                            if (gelesener_exponent == datensatzende) eingabeende = true;
                        } else {
                            koeff_zu_exponent[gelesener_exponent] = gelesener_koeffizient;
                        }
                    }
                } while (!eingabeende);
            }
            if (!(falscheeingabe || ende)) {
                float n = Math.round((obere_grenze - untere_grenze) / teilintervallbreite);
                for (int k = 1; k <= n; k++) {
                    float xk_fuer_kminus1 = untere_grenze + ((obere_grenze - untere_grenze) / n * (k - 1));
                    float xk_fuer_k = untere_grenze + ((obere_grenze - untere_grenze) / n * k);
                    float x_fuer_k = (xk_fuer_kminus1 + xk_fuer_k) / 2;
                    abbildung_fvonxfuerk = 0;
                    for (int exp = 0; exp <= hoechster_exponent; exp++) {
                        abbildung_fvonxfuerk += koeff_zu_exponent[exp] * (Math.pow(x_fuer_k, exp));
                    }
                    summe_fvonx += abbildung_fvonxfuerk;
                }
                float ergebnis = (obere_grenze - untere_grenze) / n * summe_fvonx;
                Fixedln(ergebnis);
                return (Ausgabe);
            } else {
                if (falscheeingabe) {
                    return ("FALSCHE EINGABE");
                }
            }
        } catch (EprogException e) {
            liesZuende();
            return ("?");
        }
        return ("");
    }
