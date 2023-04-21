    String getChannelName(int channel) {
        if (channel == 0) {
            return "Main Channel";
        }
        if (channel > 0) {
            TChatUserInfo u = People.findById(channel);
            if (u == null) {
                return "(Private channel)";
            }
            return u.Topic;
        }
        PredefinedChannel c = PredefinedChannels.findById(channel);
        if (c == null) {
            return "(Private channel)";
        }
        return c.Name;
    }
