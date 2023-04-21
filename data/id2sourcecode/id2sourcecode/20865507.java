    public static final byte[] load(FileInputStream fin) {
        byte readBuf[] = new byte[512 * 1024];
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int readCnt = fin.read(readBuf);
            while (0 < readCnt) {
                bout.write(readBuf, 0, readCnt);
                readCnt = fin.read(readBuf);
            }
            fin.close();
            return bout.toByteArray();
        } catch (Exception e) {
            Debug.warning(e);
            return new byte[0];
        }
    }
