    public float[] getChannelData(String signalName, long firstValue, long lastValue) {
        if (firstValue >= lastValue) {
            return new float[0];
        }
        float[] datos;
        float[] fuente;
        int pos1, pos2;
        if (signalName.equals("ECG")) {
            pos1 = TimePositionConverter.timeToPosition(timeOriginDate.getTime(), firstValue, ecgSamplingRate);
            pos2 = TimePositionConverter.timeToPosition(timeOriginDate.getTime(), lastValue, ecgSamplingRate);
            datos = new float[pos2 - pos1];
            fuente = this.ecg;
        } else {
            pos1 = TimePositionConverter.timeToPosition(timeOriginDate.getTime(), firstValue, hrSamplingRate);
            pos2 = TimePositionConverter.timeToPosition(timeOriginDate.getTime(), lastValue, hrSamplingRate);
            datos = new float[pos2 - pos1];
            fuente = this.heartRate;
        }
        int i;
        for (i = 0; i < datos.length && pos1 + i < fuente.length; i++) {
            datos[i] = fuente[pos1 + i];
        }
        for (; i < datos.length; i++) {
            datos[i] = 0;
        }
        return datos;
    }
