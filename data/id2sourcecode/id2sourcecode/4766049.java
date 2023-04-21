    public boolean lock() {
        try {
            if (fLock == null) {
                fLock = file.getChannel().tryLock(0, 1, false);
                return fLock != null;
            }
            return true;
        } catch (IOException ex) {
            Logger.getLogger(PIDFile.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
