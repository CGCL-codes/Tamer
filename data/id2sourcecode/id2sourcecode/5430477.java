    public void setChannel(BluetoothDevice remoteDevice, int uuid, int channel) {
        if (V) Log.v(TAG, "Setchannel for " + remoteDevice + "_" + Integer.toHexString(uuid) + " to " + channel);
        if (channel != getChannel(remoteDevice, uuid)) {
            String key = getChannelKey(remoteDevice, uuid);
            Editor ed = mChannelPreference.edit();
            ed.putInt(key, channel);
            ed.commit();
            mChannels.put(key, channel);
        }
    }
