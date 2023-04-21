    private String getChannelList(ScheduleItem item) {
        String channelList = "";
        HashMap<String, Channel> channels = store.getChannels();
        String[] keys = (String[]) channels.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        String chan = null;
        Arrays.sort(keys);
        if (item != null) chan = item.getChannel();
        for (int x = 0; x < keys.length; x++) {
            if (chan != null && keys[x].equals(chan)) channelList += "<label><input type='radio' name='channel' value='" + keys[x] + "' checked>" + keys[x] + "</label><br>\n"; else channelList += "<label><input type='radio' name='channel' value='" + keys[x] + "'>" + keys[x] + "</label><br>\n";
        }
        return channelList;
    }
