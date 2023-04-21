    public static void writeHeaderXMmsReadReply(OutputStream os, int readReplyId) throws IOException {
        WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_X_MMS_READ_REPLY);
        WspUtil.writeShortInteger(os, readReplyId);
    }
