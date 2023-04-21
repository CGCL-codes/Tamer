    private boolean checkConnection(String name) {
        Channel tempChannel = ChannelFactory.defaultFactory().getChannel(name);
        try {
            tempChannel.checkConnection();
        } catch (ConnectionException e) {
            JOptionPane.showMessageDialog(myWindow(), "Opps - I can't connect to the PV called " + name, "Connection Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
