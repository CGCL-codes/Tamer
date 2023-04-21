    @Override
    protected int exhaustMessageData(int msgNum, Writer out) throws IOException {
        if ((msgNum <= 0) || (null == out)) return Integer.MIN_VALUE;
        final char[] wb = getWorkBuf();
        if ((null == wb) || (wb.length <= 1)) return Integer.MAX_VALUE;
        final TextNetConnection conn = getTextNetConnection();
        if (null == conn) throw new IOException(ClassUtil.getArgumentsExceptionLocation(getClass(), "exhaustMessageData", Integer.valueOf(msgNum)) + " no " + TextNetConnection.class.getName() + "instance to read from");
        final EOMHunterWriter eow = new EOMHunterWriter(out, false, false);
        for (int readIndex = 0; readIndex < (Integer.MAX_VALUE - Byte.MAX_VALUE) && (!eow.isEOMDetected()); readIndex++) {
            final int readLen = conn.read(wb, 0, wb.length - EOLStyle.CRLF.length());
            if (readLen < 0) throw new IOException("Error (" + readLen + ") while reading message buffer #" + readIndex);
            if (readLen > 0) {
                wb[readLen] = '\0';
                eow.write(wb, 0, readLen);
            }
        }
        if (!eow.isEOMDetected()) throw new IOException("Virtual infinite message data read loop exit");
        eow.close();
        return 0;
    }
