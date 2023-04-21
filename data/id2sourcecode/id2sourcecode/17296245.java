    public void stop() {
        play = false;
        synchronized (playTasks) {
            for (PlayTask th : playTasks) th.playQueue.clear();
        }
        for (MidiChannel c : synthesizer.getChannels()) if (c != null) c.allNotesOff();
    }
