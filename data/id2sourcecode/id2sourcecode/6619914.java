    protected void eventListener(ChangeEvent e) {
        int v = slider.getValue();
        text.setText(Double.toString((v + base) / subDiv));
        sendSysex(v);
        try {
            Thread.sleep(100);
        } catch (Exception ex) {
        }
        ((Driver) thisPatch.getDriver()).send(sndChange(((Driver) thisPatch.getDriver()).getChannel()));
    }
