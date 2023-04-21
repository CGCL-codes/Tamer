    private void lock() throws LogException {
        try {
            out.write(lockcmd);
            out.write(url);
            out.write(LogServerThread.CHAR_EOL);
            out.flush();
        } catch (IOException ie) {
            throw new LogException(LogException.ERROR_CONN, ie);
        }
        processStatus();
        locked = true;
    }
