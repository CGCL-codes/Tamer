    public String offerta(Pacco pPacco) {
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
        String sOfferta = offerta + "";
        String offertafinale = "";
        for (int i = 0; i < sOfferta.length(); i++) {
            if (sOfferta.length() == 4) {
                if (i == 0 || i == 1) {
                    offertafinale = offertafinale + sOfferta.charAt(i);
                    if (i == 0) {
                        offertafinale = offertafinale + ".";
                    }
                } else {
                    offertafinale = offertafinale + "0";
                }
            }
            if (sOfferta.length() == 5) {
                if (i == 0 || i == 1) {
                    offertafinale = offertafinale + sOfferta.charAt(i);
                    if (i == 1) {
                        offertafinale = offertafinale + ".";
                    }
                } else {
                    offertafinale = offertafinale + "0";
                }
            }
            if (sOfferta.length() == 6) {
                if (i == 0 || i == 1) {
                    offertafinale = offertafinale + sOfferta.charAt(i);
                } else if (i == 2) {
                    offertafinale = offertafinale + ".";
                } else {
                    offertafinale = offertafinale + "0";
                }
            }
        }
        return offertafinale;
    }
