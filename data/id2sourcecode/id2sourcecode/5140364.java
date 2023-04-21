    private boolean checkToken(ByteWrapper token, InetAddress ip, int port, Key lookupKey, long timeStamp) {
        byte[] tdata = new byte[ip.getAddress().length + 2 + 8 + Key.SHA1_HASH_LENGTH + sessionSecret.length];
        ByteBuffer bb = ByteBuffer.wrap(tdata);
        bb.put(ip.getAddress());
        bb.putShort((short) port);
        bb.putLong(timeStamp);
        bb.put(lookupKey.getHash());
        bb.put(sessionSecret);
        return token.equals(new ByteWrapper(getHasher().digest(bb)));
    }
