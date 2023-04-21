    public String md5Encode(final String input) throws UnsupportedEncodingException {
        final byte digestBytes[] = md5Digester.digest(input.getBytes("8859_1"));
        final char outArray[] = new char[32];
        for (int n = 0; n < digestBytes.length; n++) {
            final int hiNibble = (digestBytes[n] & 0xFF) >> 4;
            final int loNibble = (digestBytes[n] & 0xF);
            outArray[2 * n] = (hiNibble > 9 ? (char) (hiNibble + 87) : (char) (hiNibble + 48));
            outArray[(2 * n) + 1] = (loNibble > 9 ? (char) (loNibble + 87) : (char) (loNibble + 48));
        }
        return new String(outArray);
    }
