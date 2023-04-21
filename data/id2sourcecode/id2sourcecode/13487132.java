    public static String readString(ByteBuffer byteBuffer) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        while ((read = byteBuffer.get()) != 0) {
            out.write(read);
        }
        return Utf8.convert(out.toByteArray());
    }
