    private void unlock() throws LogException {
        try {
            out.write(unlockcmd);
            out.write(LogServerThread.CHAR_EOL);
            out.flush();
        } catch (IOException ie) {
            throw new LogException(LogException.ERROR_CONN, ie);
        }
        processStatus();
        locked = false;
    }
