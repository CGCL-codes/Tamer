    public MP3Reader(FileInputStream stream) {
        fis = stream;
        channel = fis.getChannel();
        try {
            mappedFile = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        } catch (IOException e) {
            log.error("MP3Reader :: MP3Reader ::>\n", e);
        }
        mappedFile.order(ByteOrder.BIG_ENDIAN);
        in = ByteBuffer.wrap(mappedFile);
        analyzeKeyFrames();
        firstFrame = true;
        fileMeta = createFileMeta();
        if (in.remaining() > 4) {
            searchNextFrame();
            int pos = in.position();
            MP3Header header = readHeader();
            in.position(pos);
            if (header != null) {
                checkValidHeader(header);
            } else {
                throw new RuntimeException("No initial header found.");
            }
        }
    }
