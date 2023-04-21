    private void writeBuffer() throws LogException {
        if (bufidx <= 0) return;
        if (!locked) lock();
        try {
            out.write(datacmd);
            out.write(String.valueOf(bufidx).getBytes(encname));
            out.write(LogServerThread.CHAR_EOL);
            out.flush();
            out.write(buf, 0, bufidx);
            out.flush();
        } catch (IOException ie) {
            throw new LogException(LogException.ERROR_CONN, ie);
        }
        processStatus();
        bufidx = 0;
    }
