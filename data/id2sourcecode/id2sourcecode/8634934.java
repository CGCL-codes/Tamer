    public void registerUploadSession(Object ob) {
        if (ob != null) {
            boolean ok = interestedSendSessions.add(ob);
            log.finer(ok ? ("register " + ob) : ob + (" is allready registered"));
            if (ok && interestedSendSessions.size() == 1) {
                try {
                    channel = (new RandomAccessFile(myFile, READONLY)).getChannel();
                } catch (FileNotFoundException fnfe) {
                    log.severe(this + " " + fnfe.getMessage());
                    SharesManager.getInstance().remove(this);
                }
            }
        }
    }
