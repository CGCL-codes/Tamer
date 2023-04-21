    public byte[] serializeClose(CloseElement close) {
        StringBuffer buf = new StringBuffer();
        buf.append("<close number='");
        buf.append(close.getChannelNumber());
        buf.append("' code='");
        buf.append(close.getCode());
        if (close.getDiagnostic() != null) {
            if (close.getXmlLang() != null) {
                buf.append("' xml:lang='");
                buf.append(close.getXmlLang());
            }
            buf.append("'>");
            buf.append(close.getDiagnostic());
            buf.append("</close>");
        } else {
            buf.append("'/>");
        }
        return StringUtil.stringBufferToAscii(buf);
    }
