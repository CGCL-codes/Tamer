    private void brokenCheck(boolean rw) throws IOException {
        Thread thread = (rw == WRITER) ? reader : writer;
        if ((thread != null) && !thread.isAlive()) {
            throw new IOException("Broken pipe");
        }
    }
