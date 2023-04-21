    public void getChannelsOnLineup(String lineup) {
        Integer counter = 1;
        logger.Debug("Finding all the channels for lineup: " + lineup + ".");
        ChannelAPI.List allChannels = sageApi.database.GetChannelsOnLineup(lineup);
        allChannels = allChannels.Sort(false, "GetChannelNumber");
        Integer numberOfChannels = allChannels.size();
        logger.Debug(numberOfChannels.toString() + " channels found for lineup: " + lineup + ".");
        StringBuffer msgToSend = new StringBuffer();
        String channels = "", oneChannel = "";
        for (ChannelAPI.Channel channel : allChannels) {
            oneChannel = (isXML ? XMLUtil.createXMLChannel(channel, lineup) : JSONUtil.createJSONChannel(channel, lineup));
            if (!oneChannel.isEmpty()) msgToSend.append(oneChannel);
            if (counter >= numberOfChannels) {
                if (isXML) channels = "<?xml version=\"1.0\" encoding=\"utf-8\"?><Collection><Lineup name=\"" + XMLUtil.formatForXML(lineup) + "\">" + msgToSend.toString() + "</Lineup></Collection>"; else {
                    msgToSend.delete(msgToSend.length() - 2, msgToSend.length());
                    channels = "{\"Name\": \"" + JSONUtil.formatForJSON(lineup, false) + "\", \"Lineup\": [" + msgToSend.toString() + "]}";
                }
                send(new Message(MessageType.ALL_CHANNELS, channels));
                msgToSend = new StringBuffer();
            }
            counter++;
        }
    }
