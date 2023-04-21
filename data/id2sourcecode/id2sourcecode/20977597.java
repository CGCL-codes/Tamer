    @SuppressWarnings({ "ChannelOpenedButNotSafelyClosed", "IOResourceOpenedButNotSafelyClosed" })
    private boolean acquireFileLock() {
        try {
            lockChannel = new RandomAccessFile(file, "rw").getChannel();
            fileLock = lockChannel.tryLock();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XMLContainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLContainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OverlappingFileLockException ex) {
            Logger.getLogger(XMLContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
