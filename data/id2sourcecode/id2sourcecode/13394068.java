    private void removeChannelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        PortConnections pconns = PortConnections.getInstance();
        java.util.List<String> channels = pconns.getChannels();
        String channel = (String) JOptionPane.showInputDialog(null, "select the channel you want to delete", "Channel", JOptionPane.PLAIN_MESSAGE, null, channels.toArray(), channels.get(0));
        System.out.println("channel is:" + channel);
        if (channel != null) {
            java.util.List<PortConnection> plist = pconns.getPortConnections();
            boolean exists = false;
            for (PortConnection pconn : plist) {
                if (pconn.getChannel().equals(channel)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                pconns.removeChannel(channel);
                drawGraph();
            } else {
                JOptionPane.showMessageDialog(null, "channel cannot be removed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
