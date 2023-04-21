    public void sendPatch(Patch p) {
        if (NovationNova1PatchSender.bShowMenu) {
            NovationNova1PatchSender.deviceIDoffset = deviceIDoffset;
            NovationNova1PatchSender.channel = getChannel();
            NovationNova1PatchSender nps = new NovationNova1PatchSender(null, p, this);
            nps.setVisible(true);
        } else {
            sendPatchWorker(p);
        }
    }
