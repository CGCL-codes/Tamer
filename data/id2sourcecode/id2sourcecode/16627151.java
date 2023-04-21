    public void newEvent(Event event) {
        if (event instanceof CChange) {
            tcc = (CChange) event;
            if (!isLearning()) {
                if (milArr[tcc.getMidiChannel()][tcc.getControllerNum()] != null) {
                    milArr[tcc.getMidiChannel()][tcc.getControllerNum()].midiCtrlRecieved(tcc.getMidiChannel(), tcc.getControllerNum(), tcc.getValue(), tcc.getTime());
                }
            } else {
                if (learnLoc instanceof MidiInputLocation) {
                    this.setMidiInputLocation(learnLoc.getChannel(), learnLoc.getCtrlType(), tcc.getMidiChannel(), tcc.getControllerNum());
                } else if (learnLoc instanceof MidiOutputLocation) {
                    learnLoc.setMidiController(tcc.getMidiChannel(), tcc.getControllerNum());
                }
            }
        }
    }
