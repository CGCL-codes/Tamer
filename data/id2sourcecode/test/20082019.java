    byte[] doHash(byte[] byteArray1, byte[] byteArray2) {
        md.reset();
        md.update(byteArray1);
        md.update(byteArray2);
        return md.digest();
    }
