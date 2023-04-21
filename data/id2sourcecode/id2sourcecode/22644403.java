    public FLVReader(FileInputStream f, boolean generateMetadata) {
        this.fis = f;
        this.generateMetadata = generateMetadata;
        channel = fis.getChannel();
        try {
            mappedFile = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            mappedFile.order(ByteOrder.BIG_ENDIAN);
        } catch (IOException e) {
            log.error("FLVReader :: FLVReader ::>\n", e);
        }
        in = ByteBuffer.wrap(mappedFile);
        log.debug("FLVReader 1 - Buffer size: " + in.capacity() + " position: " + in.position() + " remaining: " + in.remaining());
        if (in.remaining() >= 9) {
            decodeHeader();
        }
        keyframeMeta = analyzeKeyFrames();
    }
