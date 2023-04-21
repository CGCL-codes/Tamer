    public void P107(double ta, double p, double ro) {
        double a, va, bem, rom, roo, e, ep, ek, be, ga;
        bem = compLib.atan(ta * ta / p);
        rom = p / ta * Math.tan(bem / 2);
        if (Math.abs(ro - rom) < PR) {
            spolecne.mala(2 * p / ta, ta);
            return;
        }
        if (ro > rom) {
            spolecne.chyba("ro", ">", rom);
            return;
        }
        ep = 0;
        ek = PI / 2;
        do {
            e = (ep + ek) / 2;
            va = ta * Math.sin(e);
            a = 2 * p / va;
            ga = spolecne.gata(a, ta, e);
            be = spolecne.gata(a, ta, PI - e);
            roo = a / (1 / Math.tan(be / 2) + 1 / Math.tan(ga / 2));
            if (roo < ro) ep = e; else ek = e;
        } while (Math.abs(ro - roo) < PR);
        Spolecne.Double3 x = spolecne.vabega(va, be, ga);
        spolecne.vysacb(x.a, x.b, x.c);
    }
