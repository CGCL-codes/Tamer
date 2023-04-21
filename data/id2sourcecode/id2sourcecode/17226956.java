    protected void writeMailboxState(ByteBuffer buf, int mailCount, int unreadCount, boolean hasExpress) {
        writeC(buf, 0);
        writeC(buf, mailCount);
        writeC(buf, 0);
        writeC(buf, unreadCount);
        writeC(buf, 0);
        writeC(buf, hasExpress ? 1 : 0);
        writeH(buf, 0);
        writeC(buf, 0);
    }
