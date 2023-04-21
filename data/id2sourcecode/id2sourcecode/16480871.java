    public byte[] getChannelOpenData() {
        try {
            ByteArrayWriter baw = new ByteArrayWriter();
            baw.writeString(hostToConnectOrBind);
            baw.writeInt(portToConnectOrBind);
            baw.writeString(originatingHost);
            baw.writeInt(originatingPort);
            return baw.toByteArray();
        } catch (IOException ioe) {
            return null;
        }
    }
