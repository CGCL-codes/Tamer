    public void doJoin(final String chan, final String key) {
        this.sendToServer("JOIN " + chan + " " + key);
        Controller.getInstance().getCWatcher().create(chan);
        Controller.getInstance().getClient().getChannelList().setChannelData(new Vector<String>(Controller.getInstance().getCWatcher().chanList()));
        ActiveChannel.active(chan);
    }
