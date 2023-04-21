    public void P120(double a, double b, double ua, boolean znak, String osa) {
        double c, ga, gap, gak, uaa, uami, uama;
        if (b <= a) uami = 0.0; else uami = 2 * b * (b - a) / (2 * b - a);
        uama = 2 * b * (a + b) / (a + 2 * b);
        if (ua <= uami) {
            spolecne.chyba(osa, "<=", uami);
            return;
        }
        if (ua >= uama) {
            spolecne.chyba(osa, ">=", uama);
            return;
        }
        gap = 0.0;
        gak = PI;
        do {
            ga = (gap + gak) / 2;
            c = spolecne.cs(a, b, ga);
            uaa = spolecne.osauhlu(b, c, a);
            if (uaa < ua) gap = ga; else gak = ga;
        } while (Math.abs(ua - uaa) > PR);
        spolecne.vyslbac(znak, a, b, c);
    }
