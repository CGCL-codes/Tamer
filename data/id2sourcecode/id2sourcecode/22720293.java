    private int writeBuffer(FileOutputStream out, Object buf) throws IOException {
        if (buf instanceof byte[]) {
            byte[] bytes = (byte[]) buf;
            out.write(bytes);
            return bytes.length;
        } else {
            ByteRange range = (ByteRange) buf;
            range.getBuf().position(range.getStartOffset());
            ByteBuffer slice = range.getBuf().slice();
            slice.limit(range.getSize());
            return out.getChannel().write(slice);
        }
    }
