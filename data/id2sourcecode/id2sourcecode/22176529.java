    public String toString() {
        if (af.getChannels() == 1) return mixer.getMixerInfo().getName() + " (MONO)"; else if (af.getChannels() == 2) return mixer.getMixerInfo().getName() + " (STEREO)"; else return mixer.getMixerInfo().getName() + "channels=" + af.getChannels();
    }
