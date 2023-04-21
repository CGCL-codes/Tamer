    private Set<Key> getChannelKeys(String[] channelKeyStrs) {
        Set<Key> channelKeys = new HashSet<Key>();
        if (channelKeyStrs != null) {
            for (String channelKeyStr : channelKeyStrs) {
                channelKeys.add(Utils.constructKey(Channel.class, channelKeyStr));
            }
        }
        return channelKeys;
    }
