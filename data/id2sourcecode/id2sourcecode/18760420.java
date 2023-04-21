    public int generateISS() {
        int iss = (int) System.nanoTime() / 1024;
        issMd5.reset();
        issMd5.update(srcAddress.getAddress());
        issMd5.update(dstAddress.getAddress());
        issMd5.update((byte) (dstPort / 256));
        issMd5.update((byte) dstPort);
        issMd5.update((byte) (srcPort / 256));
        issMd5.update((byte) srcPort);
        issMd5.update(issRandomBytes);
        byte[] digest = issMd5.digest();
        ByteBuffer bb = ByteBuffer.wrap(digest);
        for (int i = 0; i < 4; i++) {
            iss += bb.getInt();
        }
        return iss;
    }
