    public void updateWindowControls() {
        if (win != null) {
            Image5DWindow iWin = (Image5DWindow) win;
            iWin.getChannelControl().updateChannelSelector();
        }
    }
