    private static boolean write(final File file, final String content, final String encoding, final boolean isAppend) throws UtilException {
        return execute(new FileChannelCreator() {

            public FileChannel createFileChannel() throws IOException {
                return new FileOutputStream(file.getAbsolutePath(), isAppend).getChannel();
            }
        }, new FileChannelCallback<Boolean>() {

            public Boolean doInFileChannel(FileChannel fc) throws IOException {
                fc.write(ByteBuffer.wrap(content.getBytes(encoding)));
                return true;
            }
        });
    }
