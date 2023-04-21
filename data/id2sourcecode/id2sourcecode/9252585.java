    public String[] getChannelPreference(String channelPreferenceStr) {
        String[] preferences = new String[] { "", "" };
        if (channelPreferenceStr == null) return preferences;
        String[] tempChannelViewSytles = channelPreferenceStr.split("\\|");
        String disAbleViewStr = "";
        String ableViewStr = "";
        for (int i = 0; i < tempChannelViewSytles.length; i++) {
            if (tempChannelViewSytles[i].indexOf("_N_") == -1) ableViewStr = ableViewStr.equals("") ? tempChannelViewSytles[i] : ableViewStr + "|" + tempChannelViewSytles[i];
            if (tempChannelViewSytles[i].indexOf("_Y_") == -1) disAbleViewStr = disAbleViewStr.equals("") ? tempChannelViewSytles[i] : disAbleViewStr + "|" + tempChannelViewSytles[i];
        }
        preferences[0] = disAbleViewStr;
        preferences[1] = ableViewStr;
        return preferences;
    }
