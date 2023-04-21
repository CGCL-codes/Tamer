    protected void eventListener(ChangeEvent e) {
        int v = slider.getValue();
        text.setText(new Double((double) (v + base) / subDiv).toString());
        sendSysex(v);
        try {
            Thread.sleep(100);
        } catch (Exception ex) {
        }
        ((Driver) thisPatch.getDriver()).send(sndChange(((Driver) thisPatch.getDriver()).getChannel()));
    }
