    private void playNote() {
        try {
            Thread.sleep(100);
            ShortMessage msg = new ShortMessage();
            msg.setMessage(ShortMessage.NOTE_ON, getChannel() - 1, AppConfig.getNote(), AppConfig.getVelocity());
            send(msg);
            Thread.sleep(AppConfig.getDelay());
            msg.setMessage(ShortMessage.NOTE_ON, getChannel() - 1, AppConfig.getNote(), 0);
            send(msg);
        } catch (Exception e) {
            ErrorMsg.reportStatus(e);
        }
    }
