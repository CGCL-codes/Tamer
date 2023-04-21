    public void mouseClicked(MouseEvent evt) {
        if (overHyperlink) return;
        NowAndNextLabel srcPane;
        if (evt.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        try {
            srcPane = (NowAndNextLabel) evt.getSource();
        } catch (ClassCastException e) {
            return;
        }
        int colonPos = srcPane.getName().indexOf(":");
        if (srcPane.getName().substring(0, 7).equals("channel")) {
            String channelName = srcPane.getName().substring(colonPos + 1, srcPane.getName().length());
            Channel ch = channelManager.getChannel(channelName);
            if (chListWin == null) {
                chListWin = new ChannelListWindow(ch);
                chListWin.addCloseListener(this);
                chListWin.setScroll(true);
                chListWin.setVisible(true);
                chListWin.scrollToReference(srcPane.getAnchor());
            } else {
                chListWin.reloadWindow(ch);
                chListWin.setTitle("Today's Listing for " + ch.getAlias());
                chListWin.scrollToReference(srcPane.getAnchor());
            }
        }
    }
