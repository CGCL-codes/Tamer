    private Vector getChannelsForUpdate(boolean force) {
        Vector updateChannels = new Vector();
        Properties channelInfo = new Properties();
        try {
            channelInfo.load(new FileInputStream(new File("channels.txt")));
            baseURL = channelInfo.getProperty("baseurl");
            hideNotOnAir = (channelInfo.getProperty("hideNOA").equals("true") ? true : false);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot load channels list", "TVTray Error", JOptionPane.ERROR_MESSAGE);
            fireChannelsLoaded(false);
            return null;
        }
        ArrayList channelNames = new ArrayList();
        for (Enumeration en = channelInfo.propertyNames(); en.hasMoreElements(); ) {
            String ch = (String) en.nextElement();
            if (ch.length() > 8 && ch.substring(0, 8).equals("channel.")) {
                channelNames.add(ch.substring(ch.indexOf(".") + 1, ch.length()));
            }
        }
        for (Iterator i = channelNames.iterator(); i.hasNext(); ) {
            String chanName = (String) i.next();
            Channel chan = getChannel(chanName);
            if (chan == null) {
                String channelAlias = channelInfo.getProperty("channel." + chanName);
                try {
                    Channel ch = new Channel(baseURL + chanName + ".xml", channelAlias);
                    addChannel(ch);
                    updateChannels.add(ch);
                } catch (MalformedURLException e) {
                    JOptionPane.showMessageDialog(null, "The specified URL for " + channelAlias + "(" + baseURL + chanName + ".xml) is not valid", "TVTray Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (force || (chan.isActive() && chan.requiresUpdate())) {
                    updateChannels.add(chan);
                }
            }
        }
        return updateChannels;
    }
