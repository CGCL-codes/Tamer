    public static void wirteLogfile() {
        File logFile;
        FileWriter fstream;
        BufferedWriter bstream;
        try {
            logFile = new File(Constants.OPUS_MATSIM_TEMPORARY_DIRECTORY + Constants.MEASUREMENT_LOGFILE);
            if (!logFile.exists()) {
                logFile.createNewFile();
                log.info("Logfile " + logFile.getCanonicalPath() + " not found. Logfile created.");
            }
            fstream = new FileWriter(logFile, true);
            bstream = new BufferedWriter(fstream);
            bstream.write("Duration reading parcel table and creating facilities and zones in seconds:" + DURATION_READ_FACILITIES_URBANSIM_OUTPUT / 1000);
            bstream.write("\n");
            bstream.write("Duration reading person table and creating a population in seconds:" + DURATION_READ_PERSONS_URBANSIM_OUTPUT / 1000);
            bstream.write("\n");
            bstream.write("Duration writing travel_data.csv ins seconds:" + DURATION_WRITE_TRAVEL_DATA_MATSIM_OUTPUT / 1000);
            bstream.write("\n\n");
            bstream.flush();
            bstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
