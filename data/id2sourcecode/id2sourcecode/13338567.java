    public String format() {
        StringBuffer buffer = new StringBuffer("<disconnect session=\"" + sessionId + "\"");
        if (transferId != null) {
            buffer.append(" transfer=\"" + TalkMessageParser.encodeXMLString(transferId) + "\"");
        }
        if (transferFrom != null) {
            buffer.append(" transfer-from=\"" + TalkMessageParser.encodeXMLString(transferFrom) + "\"");
        }
        if (transcript == true) {
            buffer.append(" transcript=\"yes\"");
        }
        buffer.append(">\n");
        if (reason != null) {
            buffer.append(reason.format());
        }
        if (calledInfo != null) {
            buffer.append(calledInfo.format());
        }
        buffer.append("</disconnect>" + '\n');
        return buffer.toString();
    }
