    public void P208(double c, double ro, double ab) {
        if (ab <= c) {
            spolecne.chyba("ab", "<=", c);
            return;
        }
        double rom = c / 2 * Math.sqrt((ab - c) / (ab + c));
        if (Math.abs(ro - rom) < PR) {
            spolecne.vysledek(ab / 2, ab / 2, c);
            return;
        }
        if (ro > rom) {
            spolecne.chyba("ro", ">", rom);
            return;
        }
        double s = (ab + c) / 2;
        double a = ab / 2 + Math.sqrt(ab * ab / 4 - s * (ab - s + ro * ro / (s - c)));
        spolecne.vysbac(a, ab - a, c);
    }
