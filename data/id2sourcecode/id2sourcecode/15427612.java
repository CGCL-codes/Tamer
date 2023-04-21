    public static double[] doubles(int doubleOffset, int length, File readFrom) {
        double mapped[] = new double[length];
        FileInputStream in = null;
        try {
            try {
                in = new FileInputStream(readFrom);
                FileChannel ch = in.getChannel();
                doubles(mapped, 0, doubleOffset * DOUBLE_BYTES, length, ch);
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
