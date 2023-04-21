    private void writeSample(RIFFWriter writer, DLSSample sample) throws IOException {
        AudioFormat audioformat = sample.getFormat();
        Encoding encoding = audioformat.getEncoding();
        float sampleRate = audioformat.getSampleRate();
        int sampleSizeInBits = audioformat.getSampleSizeInBits();
        int channels = audioformat.getChannels();
        int frameSize = audioformat.getFrameSize();
        float frameRate = audioformat.getFrameRate();
        boolean bigEndian = audioformat.isBigEndian();
        boolean convert_needed = false;
        if (audioformat.getSampleSizeInBits() == 8) {
            if (!encoding.equals(Encoding.PCM_UNSIGNED)) {
                encoding = Encoding.PCM_UNSIGNED;
                convert_needed = true;
            }
        } else {
            if (!encoding.equals(Encoding.PCM_SIGNED)) {
                encoding = Encoding.PCM_SIGNED;
                convert_needed = true;
            }
            if (bigEndian) {
                bigEndian = false;
                convert_needed = true;
            }
        }
        if (convert_needed) {
            audioformat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
        }
        RIFFWriter fmt_chunk = writer.writeChunk("fmt ");
        int sampleformat = 0;
        if (audioformat.getEncoding().equals(Encoding.PCM_UNSIGNED)) sampleformat = 1; else if (audioformat.getEncoding().equals(Encoding.PCM_SIGNED)) sampleformat = 1; else if (audioformat.getEncoding().equals(AudioFloatConverter.PCM_FLOAT)) sampleformat = 3;
        fmt_chunk.writeUnsignedShort(sampleformat);
        fmt_chunk.writeUnsignedShort(audioformat.getChannels());
        fmt_chunk.writeUnsignedInt((long) audioformat.getSampleRate());
        long srate = ((long) audioformat.getFrameRate()) * audioformat.getFrameSize();
        fmt_chunk.writeUnsignedInt(srate);
        fmt_chunk.writeUnsignedShort(audioformat.getFrameSize());
        fmt_chunk.writeUnsignedShort(audioformat.getSampleSizeInBits());
        fmt_chunk.write(0);
        fmt_chunk.write(0);
        writeSampleOptions(writer.writeChunk("wsmp"), sample.sampleoptions);
        if (convert_needed) {
            RIFFWriter data_chunk = writer.writeChunk("data");
            AudioInputStream stream = AudioSystem.getAudioInputStream(audioformat, (AudioInputStream) sample.getData());
            byte[] buff = new byte[1024];
            int ret;
            while ((ret = stream.read(buff)) != -1) {
                data_chunk.write(buff, 0, ret);
            }
        } else {
            RIFFWriter data_chunk = writer.writeChunk("data");
            ModelByteBuffer databuff = sample.getDataBuffer();
            databuff.writeTo(data_chunk);
        }
        writeInfo(writer.writeList("INFO"), sample.info);
    }
