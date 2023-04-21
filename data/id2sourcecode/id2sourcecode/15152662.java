    private void playPreviewNote() {
        int selNoteVal = selectedNoteWidget.getValue();
        Driver driver = (Driver) patch.getDriver();
        int noteVal = rootNoteWidget.getValue() + selNoteVal;
        try {
            Thread.sleep(10);
            ShortMessage msg = new ShortMessage();
            msg.setMessage(ShortMessage.NOTE_ON, driver.getChannel() - 1, noteVal, AppConfig.getVelocity());
            driver.send(msg);
            Thread.sleep(10);
            msg.setMessage(ShortMessage.NOTE_ON, driver.getChannel() - 1, noteVal, 0);
            driver.send(msg);
        } catch (Exception e) {
            ErrorMsg.reportStatus(e);
        }
    }
