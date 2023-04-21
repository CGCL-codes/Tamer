    public synchronized String digest(File file) {
        SHA1Digest stomach = new SHA1Digest();
        byte[] poop = new byte[64];
        FileChannel chan = null;
        try {
            chan = (new FileInputStream(file)).getChannel();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception thrown in digest(File file)", e);
        }
        byte[] temp = new byte[4 * 1024];
        ByteBuffer _temp = ByteBuffer.wrap(temp);
        try {
            while (true) {
                int pos = _temp.position();
                int read = chan.read(_temp);
                if (read == -1) break;
                stomach.update(temp, pos, read);
                if (_temp.remaining() == 0) _temp.position(0);
            }
            chan.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception thrown in digest(File file)", e);
        }
        stomach.doFinal(poop, 0);
        return (new String(Base64.encode(poop))).substring(0, 27);
    }
