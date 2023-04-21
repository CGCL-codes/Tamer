    public void P194(double ua, double ub, double uc) {
        double ubb, ucc, al, alp, alk, e, ep, ek;
        Spolecne.Double3 x;
        if (ub == uc) {
            alp = 0.0;
            alk = PI - PR;
            do {
                al = (alp + alk) / 2;
                ubb = spolecne.osauhlu(2 * ua * Math.tan(al / 2), ua / Math.cos(al / 2), ua / Math.cos(al / 2));
                if (ubb < ub) alp = al; else alk = al;
            } while (Math.abs(ub - ubb) > 1.0E-10);
            spolecne.malux(al, ua);
            return;
        } else if (ub < uc) {
            ep = -PI / 2;
            ek = 0.0;
            do {
                e = (ep + ek) / 2;
                al = spolecne.hodnota1(spolecne.funkce1.f194, ua, e, uc, 0.0, PI - 2 * Math.abs(e), true);
                x = spolecne.abcua(ua, al, e);
                ubb = spolecne.osauhlu(x.a, x.c, x.b);
                if (ubb < ub) ep = e; else ek = e;
            } while (Math.abs(ub - ubb) > 1.0E-10);
        } else {
            ep = PI / 2;
            ek = 0.0;
            do {
                e = (ep + ek) / 2;
                al = spolecne.hodnota1(spolecne.funkce1.f194, ua, e, ub, 0.0, PI - 2 * Math.abs(e), false);
                x = spolecne.abcua(ua, al, e);
                ucc = spolecne.osauhlu(x.a, x.b, x.c);
                if (ucc < uc) ep = e; else ek = e;
            } while (Math.abs(uc - ucc) > 1.0E-10);
        }
        x = spolecne.abcua(ua, al, e);
        spolecne.vysledek(x.a, x.b, x.c);
    }
