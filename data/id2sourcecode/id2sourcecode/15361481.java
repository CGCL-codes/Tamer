    private static String encode(String text) {
        byte[] input = text.getBytes();
        byte[] output;
        d.reset();
        output = d.digest(input);
        byte[] hex = new byte[output.length * 2];
        for (int j = 0; j < output.length; j++) {
            byte b = output[j];
            hex[j * 2] = toHex((b & 0xf0) >> 4);
            hex[j * 2 + 1] = toHex(b & 0x0f);
        }
        String encoded = null;
        try {
            encoded = new String(hex, "ascii");
        } catch (UnsupportedEncodingException e) {
            assert false;
        }
        return encoded;
    }
