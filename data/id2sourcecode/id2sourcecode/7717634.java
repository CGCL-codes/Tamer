    private InputStream getFileStream(AuFileFormat auFileFormat, InputStream audioStream) throws IOException {
        AudioFormat format = auFileFormat.getFormat();
        int magic = AuFileFormat.AU_SUN_MAGIC;
        int headerSize = AuFileFormat.AU_HEADERSIZE;
        long dataSize = auFileFormat.getFrameLength();
        long dataSizeInBytes = (dataSize == AudioSystem.NOT_SPECIFIED) ? UNKNOWN_SIZE : dataSize * format.getFrameSize();
        if (dataSizeInBytes > 0x7FFFFFFFl) {
            dataSizeInBytes = UNKNOWN_SIZE;
        }
        int encoding_local = auFileFormat.getAuType();
        int sampleRate = (int) format.getSampleRate();
        int channels = format.getChannels();
        boolean bigendian = true;
        byte header[] = null;
        ByteArrayInputStream headerStream = null;
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        SequenceInputStream auStream = null;
        AudioFormat audioStreamFormat = null;
        AudioFormat.Encoding encoding = null;
        InputStream codedAudioStream = audioStream;
        codedAudioStream = audioStream;
        if (audioStream instanceof AudioInputStream) {
            audioStreamFormat = ((AudioInputStream) audioStream).getFormat();
            encoding = audioStreamFormat.getEncoding();
            if ((AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding)) || (AudioFormat.Encoding.PCM_SIGNED.equals(encoding) && bigendian != audioStreamFormat.isBigEndian())) {
                codedAudioStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioStreamFormat.getSampleRate(), audioStreamFormat.getSampleSizeInBits(), audioStreamFormat.getChannels(), audioStreamFormat.getFrameSize(), audioStreamFormat.getFrameRate(), bigendian), (AudioInputStream) audioStream);
            }
        }
        baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
        if (bigendian) {
            dos.writeInt(AuFileFormat.AU_SUN_MAGIC);
            dos.writeInt(headerSize);
            dos.writeInt((int) dataSizeInBytes);
            dos.writeInt(encoding_local);
            dos.writeInt(sampleRate);
            dos.writeInt(channels);
        } else {
            dos.writeInt(AuFileFormat.AU_SUN_INV_MAGIC);
            dos.writeInt(big2little(headerSize));
            dos.writeInt(big2little((int) dataSizeInBytes));
            dos.writeInt(big2little(encoding_local));
            dos.writeInt(big2little(sampleRate));
            dos.writeInt(big2little(channels));
        }
        dos.close();
        header = baos.toByteArray();
        headerStream = new ByteArrayInputStream(header);
        auStream = new SequenceInputStream(headerStream, codedAudioStream);
        return auStream;
    }
