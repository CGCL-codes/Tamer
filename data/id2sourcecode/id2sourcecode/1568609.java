    private void resolveCollision(MidiInputLocation mil) {
        if (milArr[mil.getChannel()][mil.getCtrlType()] != null) {
            this.milVect.remove(milArr[mil.getChannel()][mil.getCtrlType()]);
        }
        milArr[mil.getChannel()][mil.getCtrlType()] = mil;
    }
