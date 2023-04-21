    public boolean clearInput(boolean lock) {
        try {
            FileLock fl = null;
            if (lock) fl = file.getChannel().lock(1, 4, true);
            file.seek(1);
            file.writeInt(0);
            if (lock) fl.release();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(PIDFile.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
