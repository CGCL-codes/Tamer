    public String getLastRecordedVersion() {
        String line = null, ret, defaultVal = "PSE " + AppFrame.getCurrentVersionString() + " started at " + getTimeAndDatestamp();
        int start, end;
        do {
            try {
                line = writer.readLine();
                if (line == null) line = defaultVal;
            } catch (IOException e) {
                System.err.println("There was an error while reading the log file.");
                e.printStackTrace();
            }
        } while (!line.matches("^PSE\\s.+started.+"));
        start = line.indexOf("v");
        end = line.indexOf("s") - 1;
        ret = line.substring(start, end);
        return ret;
    }
