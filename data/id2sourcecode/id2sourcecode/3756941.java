    public boolean load(FileInputStream fis) {
        try {
            int version = fis.read();
            if (version != FILE_VERSION) {
                return false;
            }
            fis.read();
            FileChannel fc = fis.getChannel();
            int sz = (int) (fc.size() - fc.position());
            mBytes = new byte[sz];
            fis.read(mBytes);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
