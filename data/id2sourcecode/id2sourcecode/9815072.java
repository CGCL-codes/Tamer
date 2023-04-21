    private InputStream getFileStream(AiffFileFormat aiffFileFormat, InputStream audioStream) throws IOException {
        AudioFormat format = aiffFileFormat.getFormat();
        AudioFormat streamFormat = null;
        AudioFormat.Encoding encoding = null;
        int headerSize = aiffFileFormat.getHeaderSize();
        int fverChunkSize = aiffFileFormat.getFverChunkSize();
        int commChunkSize = aiffFileFormat.getCommChunkSize();
        int aiffLength = -1;
        int ssndChunkSize = -1;
        int ssndOffset = aiffFileFormat.getSsndChunkOffset();
        short channels = (short) format.getChannels();
        short sampleSize = (short) format.getSampleSizeInBits();
        int ssndBlockSize = (channels * sampleSize);
        int numFrames = aiffFileFormat.getFrameLength();
        long dataSize = -1;
        if (numFrames != AudioSystem.NOT_SPECIFIED) {
            dataSize = (long) numFrames * ssndBlockSize / 8;
            ssndChunkSize = (int) dataSize + 16;
            aiffLength = (int) dataSize + headerSize;
        }
        float sampleFramesPerSecond = format.getSampleRate();
        int compCode = AiffFileFormat.AIFC_PCM;
        byte header[] = null;
        ByteArrayInputStream headerStream = null;
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        SequenceInputStream aiffStream = null;
        InputStream codedAudioStream = audioStream;
        if (audioStream instanceof AudioInputStream) {
            streamFormat = ((AudioInputStream) audioStream).getFormat();
            encoding = streamFormat.getEncoding();
            if ((AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding)) || ((AudioFormat.Encoding.PCM_SIGNED.equals(encoding)) && !streamFormat.isBigEndian())) {
                codedAudioStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, streamFormat.getSampleRate(), streamFormat.getSampleSizeInBits(), streamFormat.getChannels(), streamFormat.getFrameSize(), streamFormat.getFrameRate(), true), (AudioInputStream) audioStream);
            } else if ((AudioFormat.Encoding.ULAW.equals(encoding)) || (AudioFormat.Encoding.ALAW.equals(encoding))) {
                if (streamFormat.getSampleSizeInBits() != 8) {
                    throw new IllegalArgumentException("unsupported encoding");
                }
                codedAudioStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, streamFormat.getSampleRate(), streamFormat.getSampleSizeInBits() * 2, streamFormat.getChannels(), streamFormat.getFrameSize() * 2, streamFormat.getFrameRate(), true), (AudioInputStream) audioStream);
            }
        }
        baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
        dos.writeInt(AiffFileFormat.AIFF_MAGIC);
        dos.writeInt((aiffLength - 8));
        dos.writeInt(AiffFileFormat.AIFF_MAGIC2);
        dos.writeInt(AiffFileFormat.COMM_MAGIC);
        dos.writeInt((commChunkSize - 8));
        dos.writeShort(channels);
        dos.writeInt(numFrames);
        dos.writeShort(sampleSize);
        write_ieee_extended(dos, sampleFramesPerSecond);
        dos.writeInt(AiffFileFormat.SSND_MAGIC);
        dos.writeInt((ssndChunkSize - 8));
        dos.writeInt(0);
        dos.writeInt(0);
        dos.close();
        header = baos.toByteArray();
        headerStream = new ByteArrayInputStream(header);
        aiffStream = new SequenceInputStream(headerStream, codedAudioStream);
        return aiffStream;
    }
