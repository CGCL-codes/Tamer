    private void noteOff_internal(int noteNumber, int velocity) {
        synchronized (control_mutex) {
            if (!mono) {
                if (portamento) {
                    if (portamento_lastnote_ix != 127) {
                        portamento_lastnote[portamento_lastnote_ix] = noteNumber;
                        portamento_lastnote_ix++;
                    }
                }
            }
            mainmixer.activity();
            for (int i = 0; i < voices.length; i++) {
                if (voices[i].on && voices[i].channel == channel && voices[i].note == noteNumber && voices[i].releaseTriggered == false) {
                    voices[i].noteOff(velocity);
                }
                if (voices[i].stealer_channel == this && voices[i].stealer_noteNumber == noteNumber) {
                    SoftVoice v = voices[i];
                    v.stealer_releaseTriggered = false;
                    v.stealer_channel = null;
                    v.stealer_performer = null;
                    v.stealer_voiceID = -1;
                    v.stealer_noteNumber = 0;
                    v.stealer_velocity = 0;
                    v.stealer_extendedConnectionBlocks = null;
                    v.stealer_channelmixer = null;
                }
            }
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
            play_velocity = lastVelocity[noteNumber];
            play_releasetriggered = true;
            play_delay = 0;
            current_director.noteOff(tunedKey, velocity);
        }
    }
