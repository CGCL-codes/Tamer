    public boolean writeToInput(byte[] data, boolean lock) {
        try {
            FileLock fl = null;
            if (lock) {
                fl = file.getChannel().lock(1, 4 + data.length, true);
            }
            file.seek(1);
            file.writeInt(data.length);
            file.seek(5);
            file.write(data);
            if (lock) fl.release();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(PIDFile.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
