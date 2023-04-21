    public static byte[] generateHIT(byte[] hi) {
        SHA1Digest md = new SHA1Digest();
        byte[] tag = Helpers.hexStringToByteArray(TAG);
        byte[] input = new byte[tag.length + hi.length - HostIdentity.HI_PREAMBLE_LENGTH];
        System.arraycopy(tag, 0, input, 0, tag.length);
        System.arraycopy(hi, HostIdentity.HI_PREAMBLE_LENGTH, input, tag.length, hi.length - HostIdentity.HI_PREAMBLE_LENGTH);
        byte[] hashBytes = md.digest(input);
        byte[] hit = new byte[TAGLENGTH];
        System.arraycopy(PREFIX_BYTES, 0, hit, 0, PREFIXLENGTH - 1);
        hit[PREFIXLENGTH - 1] = (byte) (PREFIX_BYTES[PREFIXLENGTH - 1] & 0xF0);
        hit[PREFIXLENGTH - 1] |= (byte) ((hashBytes[PREFIXLENGTH - 1] & 0x03) << 2);
        hit[PREFIXLENGTH - 1] |= (byte) ((hashBytes[PREFIXLENGTH] & 0xC0) >> 6);
        for (int i = PREFIXLENGTH; i < PREFIXLENGTH + 12; i++) {
            hit[i] |= (byte) ((hashBytes[i] & 0x3F) << 2);
            hit[i] |= (byte) ((hashBytes[i + 1] & 0xC0) >> 6);
        }
        return hit;
    }
