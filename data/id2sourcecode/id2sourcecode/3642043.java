    static void deactivateKey(ShortMessage message, int device) {
        if (Util.getDebugLevel() > 30) Util.debug("REMOVE FROM DEVICE:" + device);
        SortedMap<Integer, SortedSet<Key>> deviceKeys = state.activeKeys.get(device);
        final Key key = new Key(message);
        if (deviceKeys != null) {
            if (Util.getDebugLevel() > 30) Util.debug("REMOVE FROM CHANNEL:" + message.getChannel());
            SortedSet<Key> channelKeys = deviceKeys.get(message.getChannel());
            if (channelKeys != null) {
                if (Util.getDebugLevel() > 30) Util.debug("REMOVE KEY:" + key);
                channelKeys.remove(key);
            }
        }
        if (!isScreenRefreshing()) {
            Key newKey = null;
            deviceloop: for (SortedMap<Integer, SortedSet<Key>> devKeys : state.activeKeys.values()) {
                for (SortedSet<Key> channelKeys : devKeys.values()) {
                    if (channelKeys != null) {
                        if (channelKeys.contains(key)) {
                            newKey = channelKeys.tailSet(key).first();
                            break deviceloop;
                        }
                    }
                }
            }
            if (newKey == null) {
                Graphs.deactivateKey(key);
            } else {
                Graphs.activateKey(newKey);
            }
        }
    }
