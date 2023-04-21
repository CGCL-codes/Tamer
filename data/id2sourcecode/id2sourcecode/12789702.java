    private String readFile(File file) {
        String contents = null;
        RandomAccessFile rfile = null;
        FileChannel chan = null;
        try {
            rfile = new RandomAccessFile(file, "r");
            chan = rfile.getChannel();
            int size = (int) chan.size();
            ByteBuffer buf = ByteBuffer.allocate(size);
            while (buf.hasRemaining()) {
                int read = chan.read(buf);
                if (read == 0) throw new IOException("Read failure: " + file.getPath());
            }
            buf.flip();
            CharBuffer cb = decoder.decode(buf);
            contents = cb.toString();
            buf = null;
            cb = null;
        } catch (FileNotFoundException fnfe) {
            logger.info(fnfe.getMessage());
        } catch (IOException ioe) {
            StackTraceElement s[] = ioe.getStackTrace();
            StringBuffer st = new StringBuffer(ioe.getMessage()).append("\n");
            for (int i = 0; i < s.length; i++) st.append(s[i]).append("\n");
            logger.error(st.toString());
        } finally {
            try {
                if (chan != null) chan.close();
                if (rfile != null) rfile.close();
            } catch (IOException ioe) {
                StackTraceElement s[] = ioe.getStackTrace();
                StringBuffer st = new StringBuffer(ioe.getMessage()).append("\n");
                for (int i = 0; i < s.length; i++) st.append(s[i]).append("\n");
                logger.error(st.toString());
            }
            rfile = null;
            chan = null;
        }
        return contents;
    }
