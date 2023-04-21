    public void processReadings(Readings readings) throws WeatherPluginException {
        PrintWriter writer = null;
        try {
            boolean writeHeadings = !outputFile.exists();
            writer = new PrintWriter(new FileOutputStream(this.outputFile, true));
            writer.write(readings.format(writeHeadings, true, true, false));
            writer.flush();
        } catch (IOException e) {
            throw new WeatherPluginException(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
