    public static void writeHeaderXMmsReadReply(OutputStream theOs, String theReadReply) throws IOException {
        int readReplyId = StringUtil.findString(MmsConstants.X_MMS_READ_REPLY_NAMES, theReadReply.toLowerCase());
        if (readReplyId != -1) {
            writeHeaderXMmsReadReply(theOs, readReplyId);
        }
    }
