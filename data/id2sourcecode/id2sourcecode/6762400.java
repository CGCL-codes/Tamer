    public Note(MidiMessage mes) {
        if (!(mes instanceof ShortMessage)) return;
        ShortMessage mes2 = (ShortMessage) mes;
        if (mes2.getCommand() != ShortMessage.NOTE_ON && mes2.getCommand() != ShortMessage.NOTE_OFF) return;
        if (mes2.getData2() == 0) noteON = false;
        channel = mes2.getChannel();
        note = mes2.getData1();
        force = mes2.getData2();
        valid = true;
    }
