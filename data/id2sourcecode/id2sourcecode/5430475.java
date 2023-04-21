    public int getChannel(BluetoothDevice remoteDevice, int uuid) {
        String key = getChannelKey(remoteDevice, uuid);
        if (V) Log.v(TAG, "getChannel " + key);
        Integer channel = null;
        if (mChannels != null) {
            channel = mChannels.get(key);
            if (V) Log.v(TAG, "getChannel for " + remoteDevice + "_" + Integer.toHexString(uuid) + " as " + channel);
        }
        return (channel != null) ? channel : -1;
    }
