    private ChannelBuffer createHixieHandshakeContent(HttpRequest request) throws NoSuchAlgorithmException {
        String key1 = request.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_KEY1);
        String key2 = request.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_KEY2);
        int keyA = (int) (Long.parseLong(key1.replaceAll("[^0-9]", "")) / key1.replaceAll("[^ ]", "").length());
        int keyB = (int) (Long.parseLong(key2.replaceAll("[^0-9]", "")) / key2.replaceAll("[^ ]", "").length());
        long keyC = request.getContent().readLong();
        ChannelBuffer responseBuffer = ChannelBuffers.buffer(HIXIE_BUFFER_SIZE);
        responseBuffer.writeInt(keyA);
        responseBuffer.writeInt(keyB);
        responseBuffer.writeLong(keyC);
        return ChannelBuffers.wrappedBuffer(MessageDigest.getInstance("MD5").digest(responseBuffer.array()));
    }
