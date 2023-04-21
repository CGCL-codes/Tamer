    public void init(Properties properties) throws PersistenceException {
        fileName = properties.getProperty(PROPERTY_USERS_FILE_PATH, "users");
        Reader r = null;
        try {
            r = new FileReader(fileName);
        } catch (FileNotFoundException fnfe) {
            Logger.getInstance().log(Logger.SEVERE, "Users file [" + fileName + "] not found. " + fnfe.toString());
            throw new PersistenceException(fnfe.toString());
        }
        BufferedReader br = new BufferedReader(r);
        try {
            String line = null;
            do {
                line = br.readLine();
                if (line != null) {
                    FileLineRecord record = new FileLineRecord(line);
                    fileContents.addElement(record);
                    if (record.isUserRecord()) {
                        users.put(record.getUser().getNickname(), record.getUser());
                    }
                }
            } while (line != null);
        } catch (IOException ioe) {
            Logger.getInstance().log(Logger.SEVERE, "Error reading from file [" + fileName + "]" + ioe.toString());
            throw new PersistenceException(ioe.toString());
        }
        try {
            br.close();
            r.close();
        } catch (IOException ioe) {
            Logger.getInstance().log(Logger.WARNING, "Error closing file [" + fileName + "]" + ioe.toString());
        }
        writeMonitorThread = new WriteMonitorThread(fileName, fileContents);
        writeMonitorThread.setDaemon(true);
        writeMonitorThread.start();
    }
