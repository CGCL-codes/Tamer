    static void activateKey(ShortMessage message, int device) {
        SortedMap<Integer, SortedSet<Key>> deviceKeys = state.activeKeys.get(device);
        if (deviceKeys == null) {
            deviceKeys = Collections.synchronizedSortedMap(new TreeMap<Integer, SortedSet<Key>>());
            state.activeKeys.put(device, deviceKeys);
        }
        final int channel;
        SortedSet<Key> channelKeys = deviceKeys.get(channel = message.getChannel());
        if (channelKeys == null) {
            channelKeys = Collections.synchronizedSortedSet(new TreeSet<Key>());
            deviceKeys.put(channel, channelKeys);
        }
        final Key key = new Key(message);
        channelKeys.remove(key);
        channelKeys.add(key);
        if (!isScreenRefreshing()) Graphs.activateKey(key);
    }
