    private void statistiken() {
        historyswitch++;
        if (historyswitch == 3) {
            if (historycountereinmaldurch == false) {
                geldhistory[historycounter] = (int) getGeld();
                firmenwerthistory[historycounter] = (int) getFirmenwert();
                if (geldhistory[historycounter] == 0) {
                    geldhistory[historycounter] = -1;
                }
                if (firmenwerthistory[historycounter] == 0) {
                    firmenwerthistory[historycounter] = -1;
                }
                historycounter++;
                if (historycounter == Master.OPTIONEN.getMaximumDiagramm()) {
                    historycountereinmaldurch = true;
                }
            } else {
                for (int i = 0; i < Master.OPTIONEN.getMaximumDiagramm() - 1; i++) {
                    geldhistory[i] = geldhistory[i + 1];
                    firmenwerthistory[i] = firmenwerthistory[i + 1];
                }
                geldhistory[Master.OPTIONEN.getMaximumDiagramm() - 1] = (int) getGeld();
                firmenwerthistory[Master.OPTIONEN.getMaximumDiagramm() - 1] = (int) getFirmenwert();
                if (geldhistory[Master.OPTIONEN.getMaximumDiagramm() - 1] == 0) {
                    geldhistory[Master.OPTIONEN.getMaximumDiagramm() - 1] = -1;
                }
                if (firmenwerthistory[Master.OPTIONEN.getMaximumDiagramm() - 1] == 0) {
                    firmenwerthistory[Master.OPTIONEN.getMaximumDiagramm() - 1] = -1;
                }
            }
            historyswitch = 0;
        }
    }
