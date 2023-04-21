    public String[][] getChannelActions(String channel) {
        String channelActions = Raptor.getInstance().getPreferences().getString(context.getPreferencePrefix() + PreferenceKeys.CHANNEL_COMMANDS);
        String[] channelActionsArray = RaptorStringUtils.stringArrayFromString(channelActions, ',');
        String[][] result = new String[channelActionsArray.length][2];
        for (int i = 0; i < channelActionsArray.length; i++) {
            String action = channelActionsArray[i];
            action = action.replace("$channel", channel);
            action = action.replace("$userName", userName);
            result[i][0] = action;
            result[i][1] = action;
        }
        return result;
    }
