    public static String read(final File file, final String encoding) throws UtilException {
        return execute(new FileChannelCreator() {

            public FileChannel createFileChannel() throws IOException {
                return new FileInputStream(file.getAbsolutePath()).getChannel();
            }
        }, new FileChannelCallback<String>() {

            public String doInFileChannel(FileChannel fc) throws IOException {
                int size = (int) fc.size();
                MappedByteBuffer mappedByteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, size);
                Charset charset = Charset.forName(encoding);
                CharBuffer cb = charset.newDecoder().decode(mappedByteBuffer);
                return cb.toString();
            }
        });
    }
