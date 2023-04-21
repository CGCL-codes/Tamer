    public String generateId() {
        byte randomBytes[] = new byte[16];
        StringBuffer buffer = new StringBuffer();
        int resultLenBytes = 0;
        while (resultLenBytes < this.idLength) {
            getRandomBytes(randomBytes);
            randomBytes = getDigest().digest(randomBytes);
            for (int j = 0; j < randomBytes.length && resultLenBytes < this.idLength; j++) {
                byte b1 = (byte) ((randomBytes[j] & 0xf0) >> 4);
                byte b2 = (byte) (randomBytes[j] & 0x0f);
                if (b1 < 10) buffer.append((char) ('0' + b1)); else buffer.append((char) ('A' + (b1 - 10)));
                if (b2 < 10) buffer.append((char) ('0' + b2)); else buffer.append((char) ('A' + (b2 - 10)));
                resultLenBytes++;
            }
        }
        return buffer.toString();
    }
