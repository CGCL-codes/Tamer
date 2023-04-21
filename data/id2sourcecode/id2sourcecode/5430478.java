    public void removeChannel(BluetoothDevice remoteDevice, int uuid) {
        String key = getChannelKey(remoteDevice, uuid);
        Editor ed = mChannelPreference.edit();
        ed.remove(key);
        ed.commit();
        mChannels.remove(key);
    }
