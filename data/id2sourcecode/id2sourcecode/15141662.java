    public void insertUser(User user) throws PersistenceException {
        FileLineRecord record = new FileLineRecord(user);
        fileContents.addElement(record);
        users.put(user.getNickname(), user);
        writeMonitorThread.scheduleWriteAction();
    }
