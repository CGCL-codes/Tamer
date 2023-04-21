    protected boolean updateMyReceiveBufferSize(Channel channel, long currentSeq, int currentAvail) throws BEEPException {
        log.debug("--> updateMyReceiveBufferSize(" + currentSeq + ", " + currentAvail + ")");
        StringBuffer sb = new StringBuffer(Frame.MAX_HEADER_SIZE);
        sb.append(MESSAGE_TYPE_SEQ);
        sb.append(' ');
        sb.append(this.getChannelNumberAsString(channel));
        sb.append(' ');
        sb.append(Long.toString(currentSeq));
        sb.append(' ');
        sb.append(Integer.toString(currentAvail));
        sb.append(CRLF);
        try {
            if (log.isDebugEnabled()) {
                log.debug("Wrote Window Update: " + sb.toString());
            }
            OutputStream os = socket.getOutputStream();
            synchronized (writerLock) {
                os.write(StringUtil.stringBufferToAscii(sb));
                os.flush();
            }
        } catch (IOException x) {
            throw new BEEPException("Unable to send SEQ", x);
        }
        log.debug("<-- updateMyReceiveBufferSize");
        return true;
    }
