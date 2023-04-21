    public FlvWriter(final int seekTime, final String fileName) {
        this.seekTime = seekTime < 0 ? 0 : seekTime;
        this.startTime = System.currentTimeMillis();
        if (fileName == null) {
            logger.info("save file notspecified, will only consume stream");
            out = null;
            return;
        }
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            out = fos.getChannel();
            out.write(FlvAtom.flvHeader().toByteBuffer());
            logger.info("opened file for writing: {}", file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
