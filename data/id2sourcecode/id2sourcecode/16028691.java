    public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.OK_OPTION;
        if (ChannelEditor.application.getChannelParkingPanel().getListSize() > 0) {
            result = JOptionPane.showConfirmDialog(ChannelEditor.application, Messages.getString("CloseAction.2"), Messages.getString("CloseAction.3"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.NO_OPTION) {
                return;
            }
        }
        if (ChannelEditor.application.isModified()) {
            result = JOptionPane.showConfirmDialog(ChannelEditor.application, Messages.getString("CloseAction.4"), Messages.getString("CloseAction.5"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.NO_OPTION) {
                return;
            }
        }
        System.exit(0);
    }
