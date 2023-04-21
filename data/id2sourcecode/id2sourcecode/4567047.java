    public byte[] hash(InputStream in, float speed, int blocksize, long inputlength) {
        if (speed != 0) setSpeed(speed);
        int localsleeptime = sleeptime;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (in instanceof ByteArrayInputStream) localsleeptime = 0;
        DigestInputStream dis = new DigestInputStream(in, md);
        byte[] buffer = new byte[(int) bufferlength];
        try {
            if (blocksize == 0) while ((dis.read(buffer) != -1)) Misc.sleeps(localsleeptime); else {
                int bytes = 0;
                int adding = 0;
                while ((adding = dis.read(buffer, 0, (int) blocksize)) != -1) {
                    if ((bytes += adding) == blocksize) {
                        baos.write(md.digest());
                        md.reset();
                        bytes = 0;
                    }
                    Misc.sleeps(localsleeptime);
                }
            }
        } catch (IOException e) {
            log.warn("", e);
        }
        try {
            baos.write(md.digest());
        } catch (IOException e) {
            log.warn("", e);
        }
        try {
            dis.close();
        } catch (IOException e) {
            log.debug("", e);
        }
        if (blocksize != 0) {
            int length = (int) (inputlength / blocksize) + 1;
            if (baos.size() != length * md.getDigestLength()) throw new RuntimeException("Programming error, postcondition failed");
        }
        return baos.toByteArray();
    }
