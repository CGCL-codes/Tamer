    private static String generateKeyRepresentation(MessageRecipient mr) throws RecipientException {
        StringBuffer buf = new StringBuffer();
        buf.append(mr.getChannelName());
        buf.append(mr.getDeviceName());
        buf.append(mr.getAddress());
        buf.append(mr.getMSISDN());
        return buf.toString();
    }
