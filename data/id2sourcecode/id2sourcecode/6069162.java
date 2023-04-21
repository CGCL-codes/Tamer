    public Diagramm(Object[][] auswertung, Typ cmd) {
        this.n = auswertung.length - 1;
        this.m = auswertung[1].length - 1;
        this.auswertung = new Object[this.n][this.m + 1];
        String[] tmpLegends = new String[this.n];
        for (int n = 0; n < this.n; n++) {
            int betrag = 0;
            for (int m = 0; m < this.m; m++) {
                this.auswertung[n][m] = auswertung[n + 1][m];
                betrag += (Integer) this.auswertung[n][m];
            }
            this.auswertung[n][m] = betrag;
            tmpLegends[n] = auswertung[n + 1][m].toString();
        }
        this.xLegend = new String("Runde");
        this.yLegend = cmd.name();
        this.dataLegends = tmpLegends;
    }
