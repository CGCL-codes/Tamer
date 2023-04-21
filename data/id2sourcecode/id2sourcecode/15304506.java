    public M4AReader(File f) throws IOException {
        if (null == f) {
            log.warn("Reader was passed a null file");
            log.debug("{}", ToStringBuilder.reflectionToString(this));
        }
        this.file = f;
        this.fis = new MP4DataStream(new FileInputStream(f));
        channel = fis.getChannel();
        try {
            mappedFile = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        } catch (IOException e) {
            log.error("M4AReader {}", e);
        }
        in = IoBuffer.wrap(mappedFile);
        decodeHeader();
        analyzeFrames();
        firstTags.add(createFileMeta());
        createPreStreamingTags();
    }
