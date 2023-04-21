    @Override
    public QDataSet getDataSet(ProgressMonitor mon) throws Exception {
        File wavFile = getFile(mon);
        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(wavFile);
        AudioFormat audioFormat = fileFormat.getFormat();
        int headerLength = 64;
        int frameSize = audioFormat.getFrameSize();
        int frameCount = (int) ((wavFile.length() - headerLength) / frameSize);
        int bits = audioFormat.getSampleSizeInBits();
        int frameOffset = 0;
        if (params.get("offset") != null) {
            double offsetSeconds = Double.parseDouble(params.get("offset"));
            frameOffset = (int) Math.floor(offsetSeconds * audioFormat.getSampleRate());
            frameCount -= frameOffset;
        }
        if (params.get("length") != null) {
            double lengthSeconds = Double.parseDouble(params.get("length"));
            int frameCountLimit = (int) Math.floor(lengthSeconds * audioFormat.getSampleRate());
            frameCount = Math.min(frameCount, frameCountLimit);
        }
        FileInputStream fin = new FileInputStream(wavFile);
        ByteBuffer byteBuffer = fin.getChannel().map(MapMode.READ_ONLY, headerLength + frameOffset * frameSize, frameCount * frameSize);
        boolean unsigned = audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED);
        if (unsigned) throw new IllegalArgumentException("Unsupported wave file format: " + audioFormat + ", need signed.");
        if (audioFormat.getChannels() > 1) {
            throw new IllegalArgumentException("Unsupported wave file format: " + audioFormat + ", need mono.");
        }
        if (bits != 16 && bits != 8) {
            throw new IllegalArgumentException("Unsupported wave file format: " + audioFormat + ", need 8 or 16 bits.");
        }
        MutablePropertyDataSet result;
        if (bits == 16) {
            byteBuffer.order(audioFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
            ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
            short[] buf = new short[frameCount];
            shortBuffer.get(buf);
            result = SDataSet.wrap(buf);
        } else {
            byte[] buf = new byte[frameCount];
            byteBuffer.get(buf);
            result = BDataSet.wrap(buf);
        }
        MutablePropertyDataSet timeTags = DataSetUtil.tagGenDataSet(frameCount, 0., 1. / audioFormat.getSampleRate(), Units.seconds);
        result.putProperty(QDataSet.DEPEND_0, timeTags);
        return result;
    }
