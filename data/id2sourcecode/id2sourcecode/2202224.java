    public void P191(double ua, double ub, double p) {
        double ubb, e, ep, ek;
        ep = -PI / 2 + PR;
        ek = PI / 2;
        do {
            e = (ep + ek) / 2;
            Spolecne.Double3 x = spolecne.abcua(ua, spolecne.alpua(ua, p, e), e);
            ubb = spolecne.osauhlu(x.a, x.c, x.b);
            if (ubb < ub) ep = e; else ek = e;
        } while (Math.abs(ub - ubb) > 1.0E-10);
        Spolecne.Double3 x = spolecne.abcua(ua, spolecne.alpua(ua, p, e), e);
        spolecne.vysledek(x.a, x.b, x.c);
    }
