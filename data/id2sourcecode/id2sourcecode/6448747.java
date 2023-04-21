    private void playNote() {
        try {
            Thread.sleep(100);
            ShortMessage msg = new ShortMessage();
            msg.setMessage(ShortMessage.NOTE_ON, getChannel() - 1, appConfig.getNote(), appConfig.getVelocity());
            send(msg);
            Thread.sleep(appConfig.getDelay());
            msg.setMessage(ShortMessage.NOTE_ON, getChannel() - 1, appConfig.getNote(), 0);
            send(msg);
        } catch (Exception e) {
            Logger.reportStatus(e);
        }
    }
