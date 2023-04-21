    private byte[] responseDigest(String key1, String key2, ByteBuffer[] body) {
        int part1 = part(key1);
        int part2 = part(key2);
        ByteBuffer b = ByteBuffer.allocate(16);
        b.asIntBuffer().put(part1);
        b.position(4);
        b.asIntBuffer().put(part2);
        b.position(8);
        for (ByteBuffer buffer : body) {
            b.put(buffer);
        }
        b.flip();
        byte[] response = null;
        synchronized (messageDigest) {
            messageDigest.update(b.array(), b.position(), b.limit());
            response = messageDigest.digest();
        }
        return response;
    }
