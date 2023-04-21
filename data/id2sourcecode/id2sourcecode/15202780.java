    private void do_write(CCNOutputStream ostream, InputStream is) throws IOException {
        long time = System.currentTimeMillis();
        int size = CommonParameters.BLOCK_SIZE;
        int readLen = 0;
        byte[] buffer = new byte[CommonParameters.BLOCK_SIZE];
        if (Log.isLoggable(Level.FINER)) {
            Log.finer("do_write: " + is.available() + " bytes left.");
            while ((readLen = is.read(buffer, 0, size)) != -1) {
                ostream.write(buffer, 0, readLen);
                Log.finer("do_write: wrote " + size + " bytes.");
                Log.finer("do_write: " + is.available() + " bytes left.");
            }
        } else {
            while ((readLen = is.read(buffer, 0, size)) != -1) {
                ostream.write(buffer, 0, readLen);
            }
        }
        ostream.close();
        Log.fine("finished write: {0}", System.currentTimeMillis() - time);
    }
