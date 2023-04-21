    protected void initMusic() {
        try {
            this.synth = MidiSystem.getSynthesizer();
            this.synth.open();
            this.synth.loadAllInstruments(this.synth.getDefaultSoundbank());
            this.channels = this.synth.getChannels();
            if (this.channels != null) {
                this.initChannels();
                this.READY = true;
            }
        } catch (javax.sound.midi.MidiUnavailableException e) {
            System.err.println("MidiUnavailableException: " + e.getMessage());
            this.READY = false;
        } catch (NullPointerException e) {
            System.err.println("Midi Initialization Error: " + e.getMessage());
            this.READY = false;
        }
    }
