    public final ID createID(byte[] testBytes) {
        synchronized (this.messageDigest) {
            this.messageDigest.reset();
            this.messageDigest.update(testBytes);
            byte[] digest = this.messageDigest.digest();
            byte[] newdigest = new byte[1];
            newdigest[0] = new Integer(((digest[4] & 0x7) << 4) + (digest[0] & 0xf)).byteValue();
            return new ID(newdigest);
        }
    }
