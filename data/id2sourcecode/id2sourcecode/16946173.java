    protected void parseEvent(MidiEvent ev) {
        MidiMessage msg = ev.getMessage();
        if (isChannel(msg)) {
            int chan = uk.org.toot.midi.message.NoteMsg.getChannel(msg);
            if (this.channel < 0) {
                this.channel = chan;
            } else if (this.channel != chan) {
                System.err.println("Track " + getTrackName() + " has more than 1 channel!");
            }
            switch(getCommand(msg)) {
                case PROGRAM_CHANGE:
                    program = getData1(msg);
                    break;
                case CONTROL_CHANGE:
                    if (BANK_SELECT == getData1(msg)) {
                        bank = getData2(msg);
                    }
                    break;
            }
        }
    }
