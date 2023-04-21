    public RequestLoginRestart() {
        writeC(0x07);
        writeS(LoginServerThread.getInstance().getServerName());
    }
