    public boolean channelNamesOK() {
        boolean isOK = true;
        Set<String> names = new HashSet();
        boolean illegalChannelNameFound = false;
        for (ConnectionChannelSelectorPanel currPanel : panels) {
            String channelName = currPanel.getConnectionChannelConfig().getChannelname();
            if (channelName.length() == 0) {
                illegalChannelNameFound = true;
                break;
            }
            names.add(channelName);
        }
        if (names.size() != panels.size() || illegalChannelNameFound) {
            JOptionPane.showMessageDialog(GlobalsLocator.getMainFrame(), GlobalsLocator.translate("connection-channelnames-warning"));
            isOK = false;
        }
        return isOK;
    }
