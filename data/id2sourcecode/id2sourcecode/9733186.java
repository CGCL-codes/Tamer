    public void finishedRun() {
        JMIDI.getChannel(JWM.getChannel()).allNotesOff();
        JWondrousMachine.MACHINES.remove(JWM);
        JWM = new JWondrousMachine(JWM);
        JWM.addListener(this);
    }
