    public static void transferBytesFully(DataInput in, DataOutput out, long count) throws IOException {
        CheckIOArg.count(count);
        if (count == 0) return;
        long transferred = 0;
        while (transferred < count) {
            out.write(in.readByte());
            transferred++;
        }
    }
