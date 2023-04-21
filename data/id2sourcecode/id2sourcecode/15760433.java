    public static WavVectorDataSet createFromFile(File wavFile) throws FileNotFoundException, IOException {
        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(wavFile);
            AudioFormat audioFormat = fileFormat.getFormat();
            FileInputStream fin = new FileInputStream(wavFile);
            ByteBuffer buf = fin.getChannel().map(MapMode.READ_ONLY, 64, wavFile.length() - 64);
            return createWavVectorDataSet(buf, audioFormat);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }
