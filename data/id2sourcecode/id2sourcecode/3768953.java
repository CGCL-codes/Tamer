    public ControlMsg(MidiMessage m) {
        if (!(m instanceof ShortMessage)) return;
        ShortMessage m2 = (ShortMessage) m;
        if (m2.getCommand() != ShortMessage.CONTROL_CHANGE) return;
        channel = m2.getChannel();
        control = m2.getData1();
        value = m2.getData2();
        System.out.println("value " + m2.getData2());
        valid = true;
    }
