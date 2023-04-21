    public static void main(String[] args) throws IOException {
        String resource = args[0];
        FileInputStream input = null;
        FileChannel channel = null;
        input = new FileInputStream(resource);
        channel = input.getChannel();
        int fileLength = (int) channel.size();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileLength);
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetDecoder decoder = charset.newDecoder();
        char[] cr = {};
        for (int i = 0; i < cr.length; i++) {
            System.out.print(cr[i]);
        }
    }
