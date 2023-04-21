    private void trackIntoParts(Track t, Part[] p, long[][][] nons) {
        for (int i = 0; i < t.size(); i++) {
            MidiEvent ev = t.get(i);
            ShortMessage mess;
            if (ev.getMessage() instanceof ShortMessage) {
                mess = (ShortMessage) ev.getMessage();
                if (mess.getCommand() == ShortMessage.NOTE_ON && mess.getData2() > 0) {
                    if (nons[mess.getChannel()][mess.getData1()][0] != -1) {
                        p("WARNING: two note on " + "messages with same pitch " + mess.getData1() + " chan = " + mess.getChannel() + " vel = " + mess.getData2() + " date orig " + nons[mess.getChannel()][mess.getData1()][1] * 1.0 / 96.0 + " date new " + ev.getTick() * 1.0 / 96.0 + ". creating new note.");
                        createNote(nons, (ev.getTick() * 1.0 / this.res * 1.0) * 0.9, mess, p);
                    }
                    nons[mess.getChannel()][mess.getData1()][0] = mess.getData2();
                    nons[mess.getChannel()][mess.getData1()][1] = ev.getTick();
                } else if (mess.getCommand() == ShortMessage.NOTE_OFF || (mess.getCommand() == ShortMessage.NOTE_ON && mess.getData2() == 0)) {
                    if (nons[mess.getChannel()][mess.getData1()][0] == -1) {
                        System.out.println(" note off message " + mess.getData1() + ", without a note on message!");
                    } else {
                        createNote(nons, (ev.getTick() * 1.0 / this.res * 1.0), mess, p);
                    }
                } else if (mess.getCommand() == ShortMessage.PROGRAM_CHANGE) {
                    p[mess.getChannel()].setInstrument(mess.getData1());
                }
            }
        }
    }
