    public void doQuit() {
        for (String name : this.channelMap.keySet()) {
            ChatChannelFrame frame = channelMap.get(name);
            this.channelMap.remove(frame.getChannelName());
            frame.dispose();
            this.getDesktopPane().remove(frame);
        }
        this.channelList.removeAllItems();
        this.deattach();
        this.dispose();
    }
