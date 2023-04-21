    public FlvWriter(int seekTime, String fileName) {
        status = new WriterStatus(seekTime);
        try {
            File file = new File(fileName);
            fos = new FileOutputStream(file);
            channel = fos.getChannel();
            logger.info("opened file for writing: " + file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        out = ByteBuffer.allocate(1024);
        out.setAutoExpand(true);
        writeHeader();
    }
