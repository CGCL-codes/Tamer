    public double hodnota(Spolecne.D3 F, double x, double y, double hodn, double m1, double m2) {
        double uhel, fm;
        int iter = 0;
        do {
            uhel = (m1 + m2) / 2;
            fm = F.f(x, y, uhel);
            if (fm < hodn) m1 = uhel; else m2 = uhel;
            iter = iter + 1;
            if (iter > 100) break;
        } while (Math.abs(hodn - fm) > 1.0E-10);
        return uhel;
    }
