    private MapsGeneratorMain() throws Exception {
        SwingEngine<MapsGeneratorMain> engine = new SwingEngine<MapsGeneratorMain>(this);
        URL configFileURL = this.getClass().getClassLoader().getResource("config/4ct-v2.xml");
        engine.render(configFileURL).setVisible(false);
        URL soundbankURL = this.getClass().getClassLoader().getResource("config/soundbank-deluxe.gm");
        if (soundbankURL.getProtocol().equals("jar")) {
            soundbank = MidiSystem.getSoundbank(soundbankURL);
        } else {
            File soundbankFile = new File(soundbankURL.toURI());
            soundbank = MidiSystem.getSoundbank(soundbankFile);
        }
        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        synthesizer.loadAllInstruments(soundbank);
        instruments = synthesizer.getLoadedInstruments();
        midiChannels = synthesizer.getChannels();
        setInstrumentsNames(colorOneInstrument, instruments);
        setInstrumentsNames(colorTwoInstrument, instruments);
        setInstrumentsNames(colorThreeInstrument, instruments);
        setInstrumentsNames(colorFourInstrument, instruments);
        colorOne = new Color(255, 99, 71);
        colorTwo = new Color(50, 205, 50);
        colorThree = new Color(238, 238, 0);
        colorFour = new Color(176, 196, 222);
        selectColorOne.setForeground(colorOne);
        selectColorTwo.setForeground(colorTwo);
        selectColorThree.setForeground(colorThree);
        selectColorFour.setForeground(colorFour);
        new Thread(refreshManager).start();
        initMapExplorerForGraphic();
        RepaintManager.currentManager(this).setDoubleBufferingEnabled(true);
        validate();
        setVisible(true);
    }
