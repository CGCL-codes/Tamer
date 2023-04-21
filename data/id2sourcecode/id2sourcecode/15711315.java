    protected AudioFileFormat getAudioFileFormat(InputStream inputStream, long lFileLengthInBytes) throws UnsupportedAudioFileException, IOException {
        DataInputStream dis = new DataInputStream(inputStream);
        int magic = dis.readInt();
        if (magic != WaveTool.WAVE_RIFF_MAGIC) {
            throw new UnsupportedAudioFileException("not a WAVE file: wrong header magic");
        }
        long totalLength = readLittleEndianInt(dis) & 0xFFFFFFFF;
        magic = dis.readInt();
        if (magic != WaveTool.WAVE_WAVE_MAGIC) {
            throw new UnsupportedAudioFileException("not a WAVE file: wrong header magic");
        }
        List<AbstractAudioChunk> chunks = parseChunks(dis, chunkParserMap);
        WaveFmtChunk wfc = (WaveFmtChunk) findChunk(chunks, WaveFmtChunk.class);
        WaveDataChunk wdc = (WaveDataChunk) findChunk(chunks, WaveDataChunk.class);
        if (wfc == null || wdc == null) {
            UnsupportedAudioFileException e = new UnsupportedAudioFileException("unsupported WAVE file: required chunk not found.");
            if (TDebug.TraceAllExceptions) {
                TDebug.out(e);
            }
            throw e;
        }
        if (dis.markSupported()) dis.reset();
        Map<String, Object> m = makeChunkProperties(chunks);
        TAudioFormat format = new TAudioFormat(wfc.getEncoding(), wfc.getFrameRate(), wfc.getSampleSizeInBits(), wfc.getChannelCount(), wfc.getFrameSize(), wfc.getFrameRate(), false, m);
        long frameLength = wdc.getDataChunkLength() / format.getFrameSize();
        return new TAudioFileFormat(AudioFileFormat.Type.WAVE, format, (int) frameLength, (int) (totalLength + WaveTool.CHUNK_HEADER_SIZE));
    }
