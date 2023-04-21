    public static char[] chars(int charOffset, int length, File readFrom) {
        char mapped[] = new char[length];
        FileInputStream in = null;
        try {
            try {
                in = new FileInputStream(readFrom);
                FileChannel ch = in.getChannel();
                chars(mapped, 0, charOffset * CHAR_BYTES, length, ch);
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException e) {
            throw new Error(e);
        }
        return mapped;
    }
