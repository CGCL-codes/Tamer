    private void createSynth() {
        FluidsynthSound sound = getElement();
        removeProblem(Severity.ERROR, "audioDriver");
        removeProblem(Severity.ERROR, "soundfont");
        try {
            synth = new Fluidsynth(name(sound.getName()), sound.getCores(), sound.getChannels(), sound.getPolyphony(), sound.getSampleRate(), sound.getAudioDriver(), sound.getAudioDevice(), sound.getAudioBuffers(), sound.getAudioBufferSize(), sound.getOverflowAge(), sound.getOverflowPercussion(), sound.getOverflowReleased(), sound.getOverflowSustained(), sound.getOverflowVolume());
            clone = (FluidsynthSound) sound.clone();
        } catch (IOException e) {
            addProblem(Severity.ERROR, "audioDriver", "create");
            return;
        } catch (NoClassDefFoundError failure) {
            addProblem(Severity.ERROR, "audioDriver", "fluidsynthFailure");
            return;
        }
        if (sound.getSoundfont() != null) {
            try {
                synth.soundFontLoad(resolve(sound.getSoundfont()));
            } catch (IOException ex) {
                addProblem(Severity.ERROR, "soundfont", "soundfontLoad", sound.getSoundfont());
            }
        }
        synth.setGain(sound.getGain() * 2.0f);
        synth.setInterpolate(sound.getInterpolate().number());
    }
