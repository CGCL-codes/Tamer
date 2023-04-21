    public final int getDeviceID() {
        int deviceID = prefs.getInt("deviceID", -1);
        return deviceID == -1 ? getChannel() : deviceID;
    }
