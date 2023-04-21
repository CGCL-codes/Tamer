    public static byte[] createAuHeader(javax.sound.sampled.AudioFormat f) {
        byte[] result = new byte[4 * 6];
        encodeIntBE(0x2e736e64, result, 0);
        encodeIntBE(result.length, result, 4);
        encodeIntBE(0xffffffff, result, 8);
        final int encoding;
        if (f.getEncoding() == Encoding.ALAW) {
            if (f.getSampleSizeInBits() == 8) encoding = 27; else return null;
        } else if (f.getEncoding() == Encoding.ULAW) {
            if (f.getSampleSizeInBits() == 8) encoding = 1; else return null;
        } else if (f.getEncoding() == Encoding.PCM_SIGNED) {
            if (f.getSampleSizeInBits() == 8) encoding = 2; else if (f.getSampleSizeInBits() == 16) encoding = 3; else if (f.getSampleSizeInBits() == 24) encoding = 4; else if (f.getSampleSizeInBits() == 32) encoding = 5; else return null;
            if (f.getSampleSizeInBits() > 8 && !f.isBigEndian()) return null;
        } else if (f.getEncoding() == Encoding.PCM_UNSIGNED) {
            return null;
        } else {
            return null;
        }
        encodeIntBE(encoding, result, 12);
        if (f.getSampleRate() < 0) return null;
        encodeIntBE((int) f.getSampleRate(), result, 16);
        if (f.getChannels() < 0) return null;
        encodeIntBE(f.getChannels(), result, 20);
        return result;
    }
