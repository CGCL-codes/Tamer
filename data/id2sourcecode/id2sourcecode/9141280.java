    private void closing(javax.swing.event.InternalFrameEvent evt) {
        int opt = JOptionPane.showConfirmDialog(this, "Do you want to quit the channel?", "Confirmination", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            server.partChannel(channel, "Requested (Closing - jIRC)");
            for (ServerWithWnd server_ : Core.mainApp.getServerList()) {
                if (server_.server == this.server) {
                    server_.wnd.getChannelWindows().remove(this.channel);
                }
            }
        }
        this.dispose();
    }
