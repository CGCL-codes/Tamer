    protected void playPatch(Patch p) {
        byte sysex[] = new byte[1182];
        System.arraycopy(((Patch) p).sysex, 0, sysex, 0, 1180);
        sysex[6] = 0;
        sysex[7] = 127;
        sysex[1180] = (byte) (0xC0 + getChannel() - 1);
        sysex[1181] = (byte) 127;
        Patch p2 = new Patch(sysex);
        try {
            super.playPatch(p2);
        } catch (Exception e) {
            ErrorMsg.reportStatus(e);
        }
    }
