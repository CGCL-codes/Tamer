    public static int[] ints(int intOffset, int length, File readFrom) {
        int mapped[] = new int[length];
        FileInputStream in = null;
        try {
            try {
                in = new FileInputStream(readFrom);
                FileChannel ch = in.getChannel();
                ints(mapped, 0, intOffset * INT_BYTES, length, ch);
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
