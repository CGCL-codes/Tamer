    public FLVReader(File f, boolean generateMetadata) throws IOException {
        if (null == f) {
            log.warn("Reader was passed a null file");
            log.debug("{}", org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this));
        }
        this.file = f;
        this.fis = new FileInputStream(f);
        this.generateMetadata = generateMetadata;
        channel = fis.getChannel();
        channelSize = channel.size();
        in = null;
        fillBuffer();
        postInitialize();
    }
