    public ArrayList<Channel> getChannelList(boolean replaceSpaces) {
        ArrayList<Channel> channels = cmd.getChannelList();
        if (replaceSpaces) {
            ArrayList<Channel> result = new ArrayList<Channel>();
            Iterator<Channel> iter = channels.iterator();
            while (iter.hasNext()) {
                Channel channel = iter.next();
                channel.setName(channel.getName().replace(" ", "_"));
                result.add(channel);
            }
            return result;
        } else {
            return channels;
        }
    }
