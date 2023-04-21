    public static void handleFileWrite(String path, Handler<FileChannel> handler) {
        FileOutputStream os = null;
        FileChannel channel = null;
        try {
            File file = ResourceUtils.getFile(path);
            os = new FileOutputStream(file);
            channel = os.getChannel();
            handler.doWith(channel);
        } catch (FileNotFoundException e) {
            logger.error("不能创建文件 " + path, e);
            throw new RuntimeException("不能创建文件 " + path, e);
        } finally {
            closeFileChannel(channel);
            IOUtils.close(os);
        }
    }
