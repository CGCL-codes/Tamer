    public ImportDrums() {
        for (int i = 0; i < MIDIBeast.allParts.size(); i++) {
            if (MIDIBeast.allParts.get(i).getChannel() == DRUM_CHANNEL) {
                MIDIBeast.drumPart = MIDIBeast.allParts.get(i);
                if (debug) {
                    System.out.println("## Initial ##\n" + MIDIBeast.drumPart);
                }
                return;
            }
        }
        MIDIBeast.drumPart = null;
        MIDIBeast.addError("Could not find a drum part.  Go to Generate-->" + "Preferences for Generation to choose a drum part from available instruments.");
        canContinue = false;
    }
