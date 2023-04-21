    public Instrumento(int numeroDeCanal) {
        try {
            this.synthesizer = MidiSystem.getSynthesizer();
            this.synthesizer.open();
            this.canal = synthesizer.getChannels()[numeroDeCanal];
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
