    private void noteOn_internal(int noteNumber, int velocity, int delay) {
        if (velocity == 0) {
            noteOff_internal(noteNumber, 64);
            return;
        }
        synchronized (control_mutex) {
            if (sustain) {
                sustain = false;
                for (int i = 0; i < voices.length; i++) {
                    if ((voices[i].sustain || voices[i].on) && voices[i].channel == channel && voices[i].active && voices[i].note == noteNumber) {
                        voices[i].sustain = false;
                        voices[i].on = true;
                        voices[i].noteOff(0);
                    }
                }
                sustain = true;
            }
            mainmixer.activity();
            if (mono) {
                if (portamento) {
                    boolean n_found = false;
                    for (int i = 0; i < voices.length; i++) {
                        if (voices[i].on && voices[i].channel == channel && voices[i].active && voices[i].releaseTriggered == false) {
                            voices[i].portamento = true;
                            voices[i].setNote(noteNumber);
                            n_found = true;
                        }
                    }
                    if (n_found) {
                        portamento_lastnote[0] = noteNumber;
                        return;
                    }
                }
                if (controller[84] != 0) {
                    boolean n_found = false;
                    for (int i = 0; i < voices.length; i++) {
                        if (voices[i].on && voices[i].channel == channel && voices[i].active && voices[i].note == controller[84] && voices[i].releaseTriggered == false) {
                            voices[i].portamento = true;
                            voices[i].setNote(noteNumber);
                            n_found = true;
                        }
                    }
                    controlChange(84, 0);
                    if (n_found) return;
                }
            }
            if (mono) allNotesOff();
            if (current_instrument == null) {
                current_instrument = synthesizer.findInstrument(program, bank, channel);
                if (current_instrument == null) return;
                if (current_mixer != null) mainmixer.stopMixer(current_mixer);
                current_mixer = current_instrument.getSourceInstrument().getChannelMixer(this, synthesizer.getFormat());
                if (current_mixer != null) mainmixer.registerMixer(current_mixer);
                current_director = current_instrument.getDirector(this, this);
                applyInstrumentCustomization();
            }
            prevVoiceID = synthesizer.voiceIDCounter++;
            firstVoice = true;
            voiceNo = 0;
            int tunedKey = (int) (Math.round(tuning.getTuning()[noteNumber] / 100.0));
            play_noteNumber = noteNumber;
            play_velocity = velocity;
            play_delay = delay;
            play_releasetriggered = false;
            lastVelocity[noteNumber] = velocity;
            current_director.noteOn(tunedKey, velocity);
        }
    }
