    private void fractalizarRectangulo(int x0, int z0, int x2, int z2, double cteRango, double cteVariacion, boolean bordesAplanados, Random random) {
        if (x0 + 1 < x2 && z0 + 1 < z2) {
            int x1 = (x0 + x2) / 2;
            int z1 = (z0 + z2) / 2;
            double h01 = 0.5d * (this.alturas[x0 + z0 * tamX] + this.alturas[x0 + z2 * tamX]);
            double h10 = 0.5d * (this.alturas[x0 + z0 * tamX] + this.alturas[x2 + z0 * tamX]);
            double h12 = 0.5d * (this.alturas[x0 + z2 * tamX] + this.alturas[x2 + z2 * tamX]);
            double h21 = 0.5d * (this.alturas[x2 + z0 * tamX] + this.alturas[x2 + z2 * tamX]);
            double h11 = 0.25d * (h01 + h10 + h12 + h21);
            if (x0 == 0 && !bordesAplanados) {
                this.alturas[x0 + z1 * tamX] += h01 + cteRango * (random.nextDouble() - 0.5d);
            }
            if (z0 == 0 && !bordesAplanados) {
                this.alturas[x1 + z0 * tamX] += h10 + cteRango * (random.nextDouble() - 0.5d);
            }
            if (!(x2 == tamX - 1 && bordesAplanados)) {
                this.alturas[x2 + z1 * tamX] += h21 + cteRango * (random.nextDouble() - 0.5d);
            }
            if (!(z2 == tamZ - 1 && bordesAplanados)) {
                this.alturas[x1 + z2 * tamX] += h12 + cteRango * (random.nextDouble() - 0.5d);
            }
            this.alturas[x1 + z1 * tamX] += h11 + cteRango * (random.nextDouble() - 0.5d);
            fractalizarRectangulo(x0, z0, x1, z1, cteRango * cteVariacion, cteVariacion, bordesAplanados, random);
            fractalizarRectangulo(x1, z0, x2, z1, cteRango * cteVariacion, cteVariacion, bordesAplanados, random);
            fractalizarRectangulo(x0, z1, x1, z2, cteRango * cteVariacion, cteVariacion, bordesAplanados, random);
            fractalizarRectangulo(x1, z1, x2, z2, cteRango * cteVariacion, cteVariacion, bordesAplanados, random);
        }
    }
