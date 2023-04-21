    private void loadPlaybackDB() {
        int numberPackets;
        Manifest manifest = Manifest.getInstance();
        manifest.loadFile(fileName.getText());
        numberPackets = manifest.getNumberPDUs();
        if (numberPackets > 0) {
            while (numberPackets % 4 == 0) numberPackets++;
            pduSlider.setMinimum(1);
            pduSlider.setMaximum(numberPackets);
            pduSlider.setLabelTable(null);
            pduSlider.setMajorTickSpacing(numberPackets / 4);
        }
    }
