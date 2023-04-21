    public void getchordPart() {
        jm.music.data.Part[] parts = score.getPartArray();
        if (debug) {
            System.out.println("Found Instrument Numbers:");
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].getChannel() == DRUM_CHANNEL) {
                    System.out.println("DRUM");
                } else {
                    System.out.println(MIDIBeast.getInstrumentForPart(parts[i]));
                }
            }
        }
        for (int i = 0; i < parts.length; i++) {
            int currentInstrument = parts[i].getInstrument();
            if (currentInstrument >= 0 && currentInstrument <= 31 && parts[i].getChannel() != DRUM_CHANNEL) {
                MIDIBeast.chordPart = parts[i];
                if (debug) {
                    System.out.println("Returning Instrument " + MIDIBeast.getInstrumentForPart(MIDIBeast.chordPart) + " as chords instrument");
                    System.out.println("Original Notes");
                    for (int j = 0; j < MIDIBeast.chordPart.getPhraseArray().length; j++) {
                        for (int k = 0; k < MIDIBeast.chordPart.getPhraseArray()[j].getNoteArray().length; k++) {
                            System.out.println(MIDIBeast.chordPart.getPhraseArray()[j].getNoteArray()[k]);
                        }
                    }
                }
                return;
            }
        }
        MIDIBeast.chordPart = null;
        MIDIBeast.addError("Could not find a chord part.  Go to Generate-->Preferences for Generation to choose a chord part from available instruments.");
        canContinue = false;
    }
