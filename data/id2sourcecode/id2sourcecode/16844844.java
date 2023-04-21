    public static byte[] createWavHeader(javax.sound.sampled.AudioFormat f) {
        if (f.getEncoding() != Encoding.PCM_SIGNED && f.getEncoding() != Encoding.PCM_UNSIGNED) return null;
        if (f.getSampleSizeInBits() == 8 && f.getEncoding() != Encoding.PCM_UNSIGNED) return null;
        if (f.getSampleSizeInBits() == 16 && f.getEncoding() != Encoding.PCM_SIGNED) return null;
        byte[] result = new byte[44];
        if (f.getSampleSizeInBits() > 8 && f.isBigEndian()) encodeIntBE(0x52494658, result, 0); else encodeIntBE(0x52494646, result, 0);
        int len = Integer.MAX_VALUE;
        encodeIntLE(len + result.length - 8, result, 4);
        encodeIntBE(0x57415645, result, 8);
        encodeIntBE(0x666d7420, result, 12);
        encodeIntLE(16, result, 16);
        encodeShortLE((short) 1, result, 20);
        encodeShortLE((short) f.getChannels(), result, 22);
        encodeIntLE((int) f.getSampleRate(), result, 24);
        encodeIntLE((((int) f.getSampleRate()) * f.getChannels() * f.getSampleSizeInBits()) / 8, result, 28);
        encodeShortLE((short) ((f.getChannels() * f.getSampleSizeInBits()) / 8), result, 32);
        encodeShortLE((short) f.getSampleSizeInBits(), result, 34);
        encodeIntBE(0x64617461, result, 36);
        encodeIntLE(len, result, 40);
        return result;
    }
