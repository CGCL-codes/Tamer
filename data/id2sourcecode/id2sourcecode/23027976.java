    public int setPageRotation(String value, int pageNumber) {
        if (debug) {
            raw_rotation = 0;
            try {
                raw_rotation = Integer.parseInt(value);
                if (raw_rotation < 0) raw_rotation = 360 + raw_rotation;
                rotationXX.setElementAt(raw_rotation, pageNumber);
            } catch (Exception e) {
                LogWriter.writeLog("Exception " + e + " reading rotation");
            }
        }
        raw_rotation = Integer.parseInt(value);
        if (raw_rotation < 0) raw_rotation = 360 + raw_rotation;
        return raw_rotation;
    }
