    public void playPatch(XMLPatch p) {
        try {
            Thread.sleep(100);
            ShortMessage msg = new ShortMessage();
            Device d = p.getDevice();
            int channel = d.getChannel() - 1;
            msg.setMessage(ShortMessage.NOTE_ON, channel, AppConfig.getNote(), AppConfig.getVelocity());
            p.getDevice().send(msg);
            Thread.sleep(AppConfig.getDelay());
            msg.setMessage(ShortMessage.NOTE_ON, channel, AppConfig.getNote(), 0);
            d.send(msg);
        } catch (Exception e) {
            ErrorMsg.reportStatus(e);
        }
    }
