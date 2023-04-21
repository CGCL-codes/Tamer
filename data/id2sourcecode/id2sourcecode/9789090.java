    public void addMeasurement(final double time, final double measuredTTMainRoute, final double measuredTTAltRoute, final double nashTime) {
        try {
            this.spreadSheetWriter.writeLine(time, measuredTTMainRoute, measuredTTAltRoute, nashTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
