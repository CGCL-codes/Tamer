    protected List parseChannelsListDisplayString(String channelsString) throws BadChangesException {
        Map allChannels = consoleManager.getChannels();
        List channels = new LinkedList();
        String[] channelNumbers = TextUtilities.getTokens(channelsString, ", ");
        for (int i = 0; i < channelNumbers.length; i++) {
            try {
                channels.add(allChannels.get(new Integer(channelNumbers[i].trim())));
            } catch (NumberFormatException e) {
                I18n i18n = I18n.get(IcsCustomConsolesPrefsPanel.class);
                throw new BadChangesException(i18n.getFormattedString("badChannelNumber", new Object[] { channelNumbers[i] }), channelsField);
            }
        }
        return channels;
    }
