    public void deleteUser(User user) throws PersistenceException {
        boolean found = false;
        int numUsers = fileContents.size();
        for (int x = 0; x < numUsers && !found; x++) {
            FileLineRecord flr = (FileLineRecord) fileContents.elementAt(x);
            if (flr.isUserRecord()) {
                if (flr.getUser().getNickname().equals(user.getNickname())) {
                    fileContents.removeElementAt(x);
                    found = true;
                }
            }
        }
        if (found) {
            writeMonitorThread.scheduleWriteAction();
        }
    }
