    @Override
    public boolean configRead(String configFile, boolean result) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true));
            if (result) {
                bw.write(createLogEntry("Config read from " + configFile + " successfully"));
            } else {
                bw.write(createLogEntry("Error! Config could not be read from " + configFile));
            }
            bw.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
