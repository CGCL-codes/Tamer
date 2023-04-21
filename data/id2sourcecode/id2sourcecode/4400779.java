    private int getStartwertNeuerSpieler() {
        int result = 0;
        Runde vorherigeRunde = getVorherigeRunde(aktuelleRunde);
        if (vorherigeRunde != null) {
            List<Spielstand> resultate = new ArrayList(vorherigeRunde.getSpielstand());
            Collections.sort(resultate);
            int lowest = resultate.get(0).getPunkte();
            int secondLowest = resultate.get(1).getPunkte();
            result = (lowest + secondLowest) / 2;
        }
        return result;
    }
