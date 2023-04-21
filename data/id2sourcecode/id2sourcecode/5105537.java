    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        if (TDebug.TraceAudioFileReader) {
            TDebug.out("MpegAudioFileReader.getAudioFileFormat(URL): begin");
        }
        long lFileLengthInBytes = AudioSystem.NOT_SPECIFIED;
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Icy-Metadata", "1");
        InputStream inputStream = conn.getInputStream();
        AudioFileFormat audioFileFormat = null;
        try {
            audioFileFormat = getAudioFileFormat(inputStream, lFileLengthInBytes);
        } finally {
            inputStream.close();
        }
        if (TDebug.TraceAudioFileReader) {
            TDebug.out("MpegAudioFileReader.getAudioFileFormat(URL): end");
        }
        return audioFileFormat;
    }
