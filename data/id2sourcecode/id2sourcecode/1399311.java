    public byte[] serializeStart(StartElement start) {
        StringBuffer buf = new StringBuffer();
        buf.append("<start number='");
        buf.append(start.getChannelNumber());
        if (start.getServerName() != null) {
            buf.append("' serverName='");
            buf.append(start.getServerName());
        }
        if (start.getProfiles().size() == 0) {
        }
        buf.append("'>");
        Iterator i = start.getProfiles().iterator();
        while (i.hasNext()) {
            serializeProfile((ProfileElement) i.next(), buf);
        }
        buf.append("</start>");
        return StringUtil.stringBufferToAscii(buf);
    }
