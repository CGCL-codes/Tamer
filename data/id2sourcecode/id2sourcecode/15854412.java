    public static <T> T handleFileRead(String path, ReturnableHandler<FileChannel, T> handler) {
        FileInputStream is = null;
        T reValue = null;
        try {
            File file = ResourceUtils.getFile(path);
            Assert.isTrue(file.exists(), "文件 [" + path + "] 不存在");
            is = new FileInputStream(file);
            FileChannel channel = null;
            try {
                channel = is.getChannel();
                reValue = handler.doWith(channel);
            } finally {
                closeFileChannel(channel);
            }
        } catch (Exception e) {
            logger.error("读取文件 [" + path + "] 时出错", e);
            throw new RuntimeException("读取文件 [" + path + "] 时出错，嵌套异常为 " + e);
        } finally {
            IOUtils.close(is);
        }
        return reValue;
    }
