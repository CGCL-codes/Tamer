    private void addChannelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String result = (String) JOptionPane.showInputDialog(null, "give the name of the channel you want to add", "Channel", JOptionPane.PLAIN_MESSAGE, null, null, "");
        if (result != null) {
            System.out.println("channel name is " + result);
            PortConnections pconns = PortConnections.getInstance();
            if (pconns.getChannels().contains(result)) {
                JOptionPane.showMessageDialog(null, "channel already exist!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                pconns.addChannel(result);
            }
        }
    }
