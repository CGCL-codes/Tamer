    protected void openDevice() {
        try {
            this.sequencer = MidiSystem.getSequencer();
            if (this.sequencer == null) {
                Debug.signal(Debug.WARNING, null, "no valid MIDI sequencers");
                this.noMusicDevice = true;
                return;
            } else {
                if (this.sequencer instanceof Synthesizer) {
                    this.synthesizer = (Synthesizer) this.sequencer;
                    this.channels = this.synthesizer.getChannels();
                }
                this.sequencer.open();
                this.sequencer.addMetaEventListener(this);
                int[] controllers = { 7 };
                this.sequencer.addControllerEventListener(this, controllers);
            }
            this.noMusicDevice = false;
        } catch (Exception ex) {
            Debug.signal(Debug.ERROR, this, "Failed to open Sound Device..." + ex);
            this.noMusicDevice = true;
        }
    }
