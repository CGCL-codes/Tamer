    public static byte[] bytes(int offset, int length, File readFrom) {
        byte mapped[] = new byte[length];
        FileInputStream in = null;
        try {
            try {
                in = new FileInputStream(readFrom);
                FileChannel ch = in.getChannel();
                bytes(mapped, 0, offset, length, ch);
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
