    public static <T> T handleFileRead(String path, ReturnableHandler<FileChannel, T> handler) {
        FileInputStream is = null;
        FileChannel channel = null;
        T reValue = null;
        try {
            File file = ResourceUtils.getFile(path);
            Assert.isTrue(file.exists(), "文件不存在");
            is = new FileInputStream(file);
            channel = is.getChannel();
            reValue = handler.doWith(channel);
        } catch (FileNotFoundException e) {
            logger.error("不能打开文件 " + path, e);
            throw new RuntimeException("不能打开文件 " + path, e);
        } finally {
            closeFileChannel(channel);
            IOUtils.close(is);
        }
        return reValue;
    }
