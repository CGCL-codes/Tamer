    public static void hexDump(final byte[] bytes, int read, final java.io.Writer writer) throws java.io.IOException {
        final int width = 16;
        for (int i = 0; i < read; i += width) {
            int limit = (i + width > read) ? read - i : width;
            int j;
            StringBuffer literals = new StringBuffer(width);
            StringBuffer hex = new StringBuffer(width * 3);
            for (j = 0; j < limit; ++j) {
                int aByte = bytes[i + j];
                if (aByte < 0) {
                    aByte = 0xff + aByte + 1;
                }
                if (aByte < 0x10) {
                    hex.append('0');
                }
                hex.append(Integer.toHexString(aByte));
                hex.append(' ');
                if (aByte >= 32 && aByte < 128) {
                    literals.append((char) aByte);
                } else {
                    literals.append('.');
                }
            }
            for (; j < width; ++j) {
                literals.append(" ");
                hex.append("-- ");
            }
            hex.append(' ');
            hex.append(literals);
            hex.append('\n');
            writer.write(hex.toString());
        }
    }
