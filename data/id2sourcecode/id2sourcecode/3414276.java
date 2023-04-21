    public static void transferBytesFullyToStream(DataInput in, OutputStream out, long count) throws IOException {
        CheckIOArg.count(count);
        if (count == 0) return;
        long transferred = 0;
        while (transferred < count) {
            out.write(in.readByte());
            transferred++;
        }
    }
