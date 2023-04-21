        public void enableMIDI() {
            try {
                sequencer = MidiSystem.getSequencer();
                if (sequencer instanceof Synthesizer) {
                    synthesizer = (Synthesizer) sequencer;
                    channels = synthesizer.getChannels();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            sequencer.addMetaEventListener(this);
            isMIDIEnabled = true;
        }
