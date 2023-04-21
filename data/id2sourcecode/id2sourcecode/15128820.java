    public JournalReader(DBBroker broker, File file, int fileNumber) throws LogException {
        this.broker = broker;
        this.fileNumber = fileNumber;
        try {
            FileInputStream is = new FileInputStream(file);
            fc = is.getChannel();
        } catch (IOException e) {
            throw new LogException("Failed to read log file " + file.getAbsolutePath(), e);
        }
    }
