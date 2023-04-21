    public void sendPatch(Patch p) {
        byte[] newsysex = new byte[264];
        System.arraycopy(((Patch) p).sysex, 0, newsysex, 0, 264);
        newsysex[4] = (byte) (0x70 + getChannel() - 1);
        newsysex[5] = (byte) (0x20);
        newsysex[6] = (byte) (0x60);
        try {
            send(newsysex);
        } catch (Exception e) {
            ErrorMsg.reportStatus(e);
        }
    }
