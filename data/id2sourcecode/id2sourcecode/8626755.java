    private static byte[] encodeString(String s, boolean use_iso) throws UnsupportedEncodingException, IOException {
        if (use_iso) return s.getBytes(CHAR_ENCODING_ISO); else {
            byte bytes[] = s.getBytes(CHAR_ENCODING_UTF_16);
            if (((0xff & bytes[0]) == 0xFE) && ((0xff & bytes[1]) == 0xFF)) {
                for (int i = 0; i < bytes.length; i += 2) {
                    byte temp = bytes[i];
                    bytes[i] = bytes[i + 1];
                    bytes[i + 1] = temp;
                }
            }
            return bytes;
        }
    }
