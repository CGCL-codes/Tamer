    public void doPart(final String msg) {
        final String myChan = getTarget();
        this.sendToServer("PART " + myChan + " " + msg);
        Controller.getInstance().getCWatcher().remove(myChan);
        Controller.getInstance().getClient().getChannelList().setChannelData(new Vector<String>(Controller.getInstance().getCWatcher().chanList()));
    }
