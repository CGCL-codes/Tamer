    public static void writeHeaderXMmsReadReply(OutputStream os, String readReply) throws IOException {
        int readReplyId = StringUtil.findString(MmsConstants.X_MMS_READ_REPLY_NAMES, readReply.toLowerCase());
        if (readReplyId != -1) {
            writeHeaderXMmsReadReply(os, readReplyId);
        }
    }
