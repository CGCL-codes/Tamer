    public static void io(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int amount;
        while ((amount = in.read(buffer)) >= 0) out.write(buffer, 0, amount);
    }
