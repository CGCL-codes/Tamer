    public void removeMidiInputLocation(MidiInputLocation mil) {
        this.milVect.remove(mil);
        milArr[mil.getChannel()][mil.getCtrlType()] = null;
    }
