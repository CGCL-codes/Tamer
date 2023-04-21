    private boolean openFile(String filename) {
        try {
            file = new RandomAccessFile(filename, "rw");
            fileChannel = file.getChannel();
            fileLock = fileChannel.tryLock();
            if (fileLock != null) {
                if (DEBUG) log("using flash file '" + filename + '\'');
                return true;
            } else {
                fileChannel.close();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeFile();
            return false;
        }
    }
