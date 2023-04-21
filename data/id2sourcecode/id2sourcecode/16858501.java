        public double calculate(String sequence) {
            this.seq = sequence;
            final double epsilon = 0.001;
            final int iterationMax = 10000;
            int counter = 0;
            double pHs = -2;
            double pHe = 16;
            double pHm = 0;
            while ((counter < iterationMax) && (Math.abs(pHs - pHe) >= epsilon)) {
                pHm = (pHs + pHe) / 2;
                final double pcs = getpI(Nterm_pI_expasy, Cterm_pI_expasy, sideGroup_pI_expasy, pHs);
                final double pcm = getpI(Nterm_pI_expasy, Cterm_pI_expasy, sideGroup_pI_expasy, pHm);
                if (pcs < 0) {
                    return pHs;
                }
                if (((pcs < 0) && (pcm > 0)) || ((pcs > 0) && (pcm < 0))) {
                    pHe = pHm;
                } else {
                    pHs = pHm;
                }
                counter++;
            }
            double pHround = Math.round(((pHs + pHe) / 2) * 100.0D);
            return (pHround / 100.0D);
        }
