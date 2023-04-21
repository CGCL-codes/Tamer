    public static byte[] getHashNio(File f) throws IOException {
        if (!f.exists()) throw new FileNotFoundException(f.toString());
        InputStream close_me = null;
        try {
            long buf_size = f.length();
            if (buf_size < 512) buf_size = 512;
            if (buf_size > 65536) buf_size = 65536;
            byte[] buf = new byte[(int) buf_size];
            FileInputStream in = new FileInputStream(f);
            close_me = in;
            FileChannel fileChannel = in.getChannel();
            ByteBuffer bb = ByteBuffer.wrap(buf);
            int read = 0;
            MD5 md5 = new MD5();
            read = fileChannel.read(bb);
            while (read > 0) {
                md5.Update(buf, 0, read);
                bb.clear();
                read = fileChannel.read(bb);
            }
            fileChannel.close();
            fileChannel = null;
            in = null;
            close_me = null;
            bb = null;
            buf = null;
            buf = md5.Final();
            md5 = null;
            return buf;
        } catch (IOException e) {
            if (close_me != null) try {
                close_me.close();
            } catch (Exception e2) {
            }
            throw e;
        }
    }
