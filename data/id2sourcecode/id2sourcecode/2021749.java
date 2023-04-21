    public void printMidiEvents(MidiEvent[] midiEventArray) {
        System.out.println("========================================MIDI EVENTS=======================================");
        System.out.println("Midi event array length = " + midiEventArray.length);
        for (int j = 0; j < midiEventArray.length; j++) {
            if (midiEventArray[j].getMessage() instanceof ShortMessage) {
                System.out.print("Tick: " + midiEventArray[j].getTick());
                ShortMessage sm = (ShortMessage) midiEventArray[j].getMessage();
                switch(sm.getCommand()) {
                    case (ShortMessage.NOTE_ON):
                        System.out.print("; Note On");
                        break;
                    case (ShortMessage.NOTE_OFF):
                        System.out.print("; Note Off");
                        break;
                    default:
                        System.out.print("; Unrecognised");
                }
                System.out.println("; Channel: " + sm.getChannel() + "; Note: " + sm.getData1() + "; Velocity: " + sm.getData2());
            } else {
                System.out.print("Tick: " + midiEventArray[j].getTick());
                System.out.println("; Not a recognised message! ");
            }
        }
        System.out.println();
    }
