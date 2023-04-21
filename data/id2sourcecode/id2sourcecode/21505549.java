    public synchronized String generateId(int length) {
        byte[] buffer = new byte[length];
        StringBuffer reply = new StringBuffer();
        int resultLenBytes = 0;
        while (resultLenBytes < length) {
            random.nextBytes(buffer);
            buffer = getDigest().digest(buffer);
            for (int j = 0; j < buffer.length && resultLenBytes < length; j++) {
                byte b1 = (byte) ((buffer[j] & 0xf0) >> 4);
                if (b1 < 10) {
                    reply.append((char) ('0' + b1));
                } else {
                    reply.append((char) ('A' + (b1 - 10)));
                }
                byte b2 = (byte) (buffer[j] & 0x0f);
                if (b2 < 10) {
                    reply.append((char) ('0' + b2));
                } else {
                    reply.append((char) ('A' + (b2 - 10)));
                }
                resultLenBytes++;
            }
        }
        return reply.toString();
    }
