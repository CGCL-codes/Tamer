    public void pause() {
        if (!play) return;
        play = false;
        for (MidiChannel c : synthesizer.getChannels()) if (c != null) c.allNotesOff();
    }
