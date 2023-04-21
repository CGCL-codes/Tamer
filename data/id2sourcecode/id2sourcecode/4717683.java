    public final int[] sign(byte[] message, boolean randomEnable) {
        messageDigest.reset();
        byte[] hash = messageDigest.digest(message);
        byte[] rndBuffer = new byte[keyLength / BYTE_SIZE - HEADER.length - HASH_SIZE / BYTE_SIZE - TAIL.length];
        if (randomEnable) random.nextBytes(rndBuffer);
        int[] blob = new int[(this.keyLength / BYTE_SIZE + INT_SIZE / BYTE_SIZE - 1) / (INT_SIZE / BYTE_SIZE)];
        Buffer.blockCopy(HEADER, 0, blob, 0, HEADER.length);
        Buffer.blockCopy(hash, 0, blob, HEADER.length, HASH_SIZE / BYTE_SIZE);
        Buffer.blockCopy(rndBuffer, 0, blob, HEADER.length + HASH_SIZE / BYTE_SIZE, rndBuffer.length);
        Buffer.blockCopy(TAIL, 0, blob, HEADER.length + HASH_SIZE / BYTE_SIZE + rndBuffer.length, TAIL.length);
        return Montgomery.montgomeryExponentiation(blob, exponent, modulus);
    }
