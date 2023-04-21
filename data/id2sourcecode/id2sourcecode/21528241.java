    public float getChannelValueAtTime(String signalName, long time) {
        int pos1 = TimePositionConverter.timeToPosition(timeOriginDate.getTime(), time, ecgSamplingRate);
        float dato;
        if (signalName.endsWith("ECG")) {
            dato = this.ecg[pos1];
        } else {
            dato = this.heartRate[pos1];
        }
        return dato;
    }
