    protected void playPatch(Patch p) {
        byte sysex[] = new byte[2352];
        System.arraycopy(p.sysex, 0, sysex, 0, 2350);
        sysex[6] = 0;
        sysex[7] = 127;
        sysex[2350] = (byte) (0xC0 + getChannel() - 1);
        sysex[2351] = (byte) 127;
        Patch p2 = new Patch(sysex);
        try {
            super.playPatch(p2);
        } catch (Exception e) {
            Logger.reportStatus(e);
        }
    }
