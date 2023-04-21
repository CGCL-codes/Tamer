    public static boolean supportsAccelerometer() {
        if (supportsSensorAPI()) {
            try {
                final javax.microedition.sensor.SensorInfo[] si = javax.microedition.sensor.SensorManager.findSensors("acceleration", javax.microedition.sensor.SensorInfo.CONTEXT_TYPE_USER);
                return si[0].getChannelInfos().length == 3;
            } catch (Throwable t) {
                return false;
            }
        } else {
            return false;
        }
    }
