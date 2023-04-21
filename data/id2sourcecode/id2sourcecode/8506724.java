    public int offertaInt(Pacco pPacco) {
        double somma = 0;
        int contatore = 0;
        for (int i = 0; i < this.premidottore.length; i++) {
            somma = somma + this.premidottore[i];
            if (this.premidottore[i] != 0) {
                contatore++;
            }
        }
        double[] prize = new double[contatore];
        int num = 0;
        for (int i = 0; i < this.premidottore.length; i++) {
            if (this.premidottore[i] != 0) {
                prize[num] = this.premidottore[i];
                num++;
            }
        }
        int media = (int) somma / contatore;
        int mediana = (int) prize[contatore / 2];
        int offerta = (media + mediana) / 2;
        if (pPacco.getPremio().equals("50.000 €") || pPacco.getPremio().equals("75.000 €") || pPacco.getPremio().equals("100.000 €") || pPacco.getPremio().equals("250.000 €") || pPacco.getPremio().equals("500.000 €")) {
            offerta = offerta + 2000;
        }
        return offerta;
    }
