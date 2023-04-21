    protected void writeMailboxState(ByteBuffer buf, int haveNewMail, int haveUnread) {
        writeC(buf, 0);
        writeC(buf, haveNewMail);
        writeC(buf, 0);
        writeC(buf, haveUnread);
        writeD(buf, 0);
        writeC(buf, 0);
    }
