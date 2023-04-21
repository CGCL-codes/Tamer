    private byte[] addChannelMapping(HTTPurl urlData) throws Exception {
        GuideStore guide = GuideStore.getInstance();
        String wsChannel = urlData.getParameter("wsChannel");
        String epgChannel = urlData.getParameter("epgChannel");
        Set<String> wsChanSet = store.getChannels().keySet();
        String[] wsChannels = (String[]) wsChanSet.toArray(new String[0]);
        boolean foundWSChannel = false;
        for (int x = 0; x < wsChannels.length; x++) {
            if (wsChannels[x].equals(wsChannel)) {
                foundWSChannel = true;
                break;
            }
        }
        String[] epgChannels = (String[]) guide.getChannelList();
        boolean foundEPGChannel = false;
        for (int x = 0; x < epgChannels.length; x++) {
            if (epgChannels[x].equals(epgChannel)) {
                foundEPGChannel = true;
                break;
            }
        }
        if (foundWSChannel && foundEPGChannel) {
            guide.addChannelToMap(wsChannel, epgChannel);
            guide.saveChannelMap(null);
        }
        StringBuffer buff = new StringBuffer(256);
        buff.append("HTTP/1.0 302 Moved Temporarily\n");
        buff.append("Location: /servlet/" + urlData.getServletClass() + "?action=04\n\n");
        return buff.toString().getBytes();
    }
