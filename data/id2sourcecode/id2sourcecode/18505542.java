    public static DataPacket sendHttpMessage(Configuration config, ServerInfo server, DataPacket source) throws IOException {
        HttpMessage mess = new HttpMessage(server.getUrl());
        if (config.isProxyNeedsAuthentication()) {
            mess.setProxyAuthorization(config.getProxyUser(), config.getProxyPassword());
        }
        byte[] serialized = source.saveToByteArray();
        InputStream is = mess.sendByteArrayInPostMessage(source.saveToByteArray());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] tmpBuffer = new byte[65536];
        int n;
        while ((n = is.read(tmpBuffer)) >= 0) baos.write(tmpBuffer, 0, n);
        is.close();
        DataPacket ret = new DataPacket();
        ret.encryptionKey = server.getEncryptionKey().getBytes();
        ret.loadFromByteArray(baos.toByteArray());
        return (ret);
    }
