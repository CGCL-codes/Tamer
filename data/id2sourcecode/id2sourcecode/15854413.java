    public static void handleFileWrite(String path, Handler<FileChannel> handler) {
        FileOutputStream os = null;
        File file = null;
        try {
            file = ResourceUtils.getFile(path);
            os = new FileOutputStream(file);
            FileChannel channel = null;
            try {
                channel = os.getChannel();
                handler.doWith(channel);
            } finally {
                closeFileChannel(channel);
            }
        } catch (Exception e) {
            if (file.exists()) {
                file.delete();
            }
            logger.error("写文件 [" + path + "] 时出错", e);
            throw new RuntimeException("写文件 [" + path + "] 时出错，嵌套异常为 " + e);
        } finally {
            IOUtils.close(os);
        }
    }
