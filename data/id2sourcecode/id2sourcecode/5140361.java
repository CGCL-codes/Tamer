    ByteWrapper genToken(InetAddress ip, int port, Key lookupKey) {
        updateTokenTimestamps();
        byte[] tdata = new byte[ip.getAddress().length + 2 + 8 + Key.SHA1_HASH_LENGTH + sessionSecret.length];
        ByteBuffer bb = ByteBuffer.wrap(tdata);
        bb.put(ip.getAddress());
        bb.putShort((short) port);
        bb.putLong(timestampCurrent.get());
        bb.put(lookupKey.getHash());
        bb.put(sessionSecret);
        return new ByteWrapper(getHasher().digest(bb));
    }
