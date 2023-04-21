    public void P183(double ta, double ua, double ub, boolean znak) {
        double ubb, ubm, al, alp, alk;
        if (ua > ta) {
            spolecne.chyba("ua", ">", ta);
            return;
        }
        if (ua == ta) {
            alp = 0;
            alk = PI / 2;
            do {
                al = (alp + alk) / 2;
                ubb = spolecne.osauhlu(2 * ta * Math.tan(al), ta / Math.cos(al), ta / Math.cos(al));
                if (ubb < ub) alp = al; else alk = al;
            } while (Math.abs(ub - ubb) < 1.0E-10);
            spolecne.malux(2 * al, ta);
            return;
        }
        Vyp183(spolecne.hodnota(spolecne.funkce1.f183, ta, ua, ub, -PI / 2, 0.0), ta, ua, znak);
        ubm = spolecne.funkce1.f183.f(ta, ua, PI / 2 - PR);
        if (ub <= ubm) return;
        Vyp183(spolecne.hodnota(spolecne.funkce1.f183, ta, ua, ub, PI / 2, 0.0), ta, ua, znak);
    }
