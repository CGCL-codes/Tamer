    public String computeChecksumSHA256(File file) {
        try {
            FileChannel chan = null;
            try {
                chan = (new FileInputStream(file)).getChannel();
            } catch (Throwable e) {
                logger.log(Level.SEVERE, "Exception thrown 1", e);
                return null;
            }
            MessageDigest sha256 = MessageDigest.getInstance("SHA256", "BC");
            byte[] temp = new byte[4 * 1024];
            ByteBuffer _temp = ByteBuffer.wrap(temp);
            try {
                while (true) {
                    int pos = _temp.position();
                    int read = chan.read(_temp);
                    if (read == -1) break;
                    sha256.update(temp, pos, read);
                    if (_temp.remaining() == 0) _temp.position(0);
                }
                chan.close();
            } catch (Throwable e) {
                logger.log(Level.SEVERE, "Exception thrown 2", e);
            }
            byte[] poop = sha256.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < poop.length; i++) {
                sb.append(Integer.toString((poop[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, "Algorithm SHA256 not supported.", ex);
        } catch (NoSuchProviderException ex) {
            logger.log(Level.SEVERE, "Provider BC not supported.", ex);
        }
        return null;
    }
