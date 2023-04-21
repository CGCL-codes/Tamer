    public static InputStream convert(InputStream stream) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat sourceFileFormat = AudioSystem.getAudioFileFormat(stream);
        AudioFormat.Encoding targetEncoding = new AudioFormat.Encoding("GSM0610");
        AudioFileFormat.Type fileType = new AudioFileFormat.Type("GSM", "gsm");
        AudioInputStream sourceStream = null;
        sourceStream = AudioSystem.getAudioInputStream(stream);
        if (sourceStream == null) {
            throw new IOException("Couldn't acquire input stream");
        }
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat.Encoding encoding = sourceFormat.getEncoding();
        if (sourceFileFormat.getType().toString().equals(fileType.toString())) {
            return stream;
        }
        if (!(encoding.equals(AudioFormat.Encoding.PCM_SIGNED) || encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED))) {
            stream = convertToPCM(sourceStream, sourceFormat);
            sourceStream.close();
            sourceStream = AudioSystem.getAudioInputStream(stream);
        }
        float sourceRate = sourceFormat.getSampleRate();
        AudioInputStream ais = null;
        if (sourceRate != 8000f) {
            AudioFileFormat.Type targetFileType = sourceFileFormat.getType();
            AudioFormat targetFormat = new AudioFormat(sourceFormat.getEncoding(), 8000f, sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), sourceFormat.getFrameSize(), 8000f, sourceFormat.isBigEndian());
            System.out.println("desired target format: " + targetFormat);
            AudioInputStream targetStream = AudioSystem.getAudioInputStream(targetFormat, sourceStream);
            System.out.println("targetStream: " + targetStream);
            int nWrittenBytes = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            nWrittenBytes = AudioSystem.write(targetStream, targetFileType, bos);
            System.out.println("Written bytes: " + nWrittenBytes);
            stream = new ByteArrayInputStream(bos.toByteArray());
            ais = AudioSystem.getAudioInputStream(stream);
            sourceFormat = ais.getFormat();
        } else {
            ais = sourceStream;
        }
        AudioInputStream gsmAIS = AudioSystem.getAudioInputStream(targetEncoding, ais);
        int nWrittenFrames = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        nWrittenFrames = AudioSystem.write(gsmAIS, fileType, bos);
        return new ByteArrayInputStream(bos.toByteArray());
    }
