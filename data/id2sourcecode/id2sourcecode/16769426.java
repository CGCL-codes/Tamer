    public void print(OutputStream stream) throws IOException {
        state = STATE.PRINTING;
        lastPrinted = System.currentTimeMillis();
        long length = accessFile.length();
        while (accessFile.getFilePointer() < length) {
            stream.write(accessFile.read());
        }
        stream.flush();
        state = STATE.READY;
    }
