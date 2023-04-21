    public void send(MidiMessage mess, long arg1) {
        ControlLaw law = cntrl.getLaw();
        ShortMessage smsg = (ShortMessage) mess;
        System.out.println("ch cmd data1 data2: " + smsg.getChannel() + " " + smsg.getCommand() + " " + smsg.getData1() + " " + smsg.getData2());
        double t = valueizer.getValue((ShortMessage) mess);
        System.out.println(" Send message to " + cntrl + " " + t);
        float val = (float) (law.getMaximum() * t + law.getMinimum() * (1 - t));
        cntrl.setValue(val);
    }
