    public String format() {
        StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"us-ascii\"?>" + '\n' + "<setup");
        if (sessionId != -1) {
            buffer.append(" session=\"" + sessionId + "\"");
        }
        if (transferId != null) {
            buffer.append(" transfer=\"" + TalkMessageParser.encodeXMLString(transferId) + "\"");
        }
        if (transferFrom != null) {
            buffer.append(" transfer-from=\"" + TalkMessageParser.encodeXMLString(transferFrom) + "\"");
        }
        if (encryptedKey != null) {
            buffer.append(" key=\"" + TalkMessageParser.encodeXMLString(encryptedKey) + "\"");
        }
        buffer.append(">\n");
        if (calling != null) {
            buffer.append(calling.format());
        }
        if (called != null) {
            buffer.append(called.format());
        }
        if (media != null) {
            buffer.append("<media>\n" + media.format() + "</media>\n");
        }
        buffer.append("</setup>\n");
        return buffer.toString();
    }
