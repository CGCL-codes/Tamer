    public static final FileChannel getOutputChannel(File file) {
        return FileUtil.getOutputStream(file).getChannel();
    }
