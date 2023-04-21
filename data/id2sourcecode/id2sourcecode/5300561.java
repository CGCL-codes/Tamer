    public void actionPerformed(ActionEvent e) {
        MainFrame frame = App.gui.getFocussedMainFrame();
        if (frame == null) {
            return;
        }
        if (currentSession == null) {
            return;
        }
        JoinDialog dialog = new JoinDialog(frame);
        dialog.setModal(true);
        dialog.setVisible(true);
        if (dialog.getResult() == JOptionPane.OK_OPTION) {
            IRCChannel[] chans = dialog.getChannels();
            IRCMessage msg;
            for (int i = 0; i < chans.length; i++) {
                msg = JoinMessage.createMessage("", "", "", chans[i].getName(), chans[i].getKey());
                currentSession.getConnection().send(msg);
            }
        }
    }
