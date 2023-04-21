        public void generateChunk(AudioFileFormat.Type fileFormat, AudioFormat format, long lLength, TDataOutputStream dos) throws IOException {
            int formatCode = WaveTool.getFormatCode(format);
            int formatChunkAdd = getFormatChunkAdd(formatCode);
            int formatChunkSize = WaveTool.FMT_CHUNK_SIZE + formatChunkAdd;
            short sampleSizeInBits;
            if (formatCode == WaveTool.WAVE_FORMAT_GSM610) sampleSizeInBits = 0; else sampleSizeInBits = (short) format.getSampleSizeInBits();
            int decodedSamplesPerBlock = getDecodedSamplesPerBlock(format);
            int avgBytesPerSec = ((int) format.getSampleRate()) / decodedSamplesPerBlock * format.getFrameSize();
            dos.writeInt(WaveTool.WAVE_FMT_MAGIC);
            dos.writeLittleEndian32(formatChunkSize);
            dos.writeLittleEndian16((short) formatCode);
            dos.writeLittleEndian16((short) format.getChannels());
            dos.writeLittleEndian32((int) format.getSampleRate());
            dos.writeLittleEndian32(avgBytesPerSec);
            dos.writeLittleEndian16((short) format.getFrameSize());
            dos.writeLittleEndian16(sampleSizeInBits);
            dos.writeLittleEndian16((short) formatChunkAdd);
            if (formatCode == WaveTool.WAVE_FORMAT_GSM610) {
                dos.writeLittleEndian16((short) decodedSamplesPerBlock);
            }
        }
