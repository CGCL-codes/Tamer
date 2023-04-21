    protected void writeHeader() throws IOException {
        if (TDebug.TraceAudioOutputStream) {
            TDebug.out("WaveAudioOutputStream.writeHeader()");
        }
        AudioFormat format = getFormat();
        long lLength = getLength();
        int formatChunkAdd = 0;
        if (formatCode == WaveTool.WAVE_FORMAT_GSM610) {
            formatChunkAdd += 2;
        }
        int dataOffset = WaveTool.DATA_OFFSET + formatChunkAdd;
        if (formatCode != WaveTool.WAVE_FORMAT_PCM) {
            dataOffset += 4 + WaveTool.CHUNK_HEADER_SIZE;
        }
        if (lLength != AudioSystem.NOT_SPECIFIED && lLength + dataOffset > 0xFFFFFFFFl) {
            lLength = 0xFFFFFFFFl - dataOffset;
        }
        long lDataChunkSize = lLength + (lLength % 2);
        TDataOutputStream dos = getDataOutputStream();
        dos.writeInt(WaveTool.WAVE_RIFF_MAGIC);
        dos.writeLittleEndian32((int) ((lDataChunkSize + dataOffset - WaveTool.CHUNK_HEADER_SIZE) & 0xFFFFFFFF));
        dos.writeInt(WaveTool.WAVE_WAVE_MAGIC);
        int formatChunkSize = WaveTool.FMT_CHUNK_SIZE + formatChunkAdd;
        short sampleSizeInBits = (short) format.getSampleSizeInBits();
        int decodedSamplesPerBlock = 1;
        if (formatCode == WaveTool.WAVE_FORMAT_GSM610) {
            if (format.getFrameSize() == 33) {
                decodedSamplesPerBlock = 320;
            } else if (format.getFrameSize() == 65) {
                decodedSamplesPerBlock = 320;
            } else {
                decodedSamplesPerBlock = (int) (format.getFrameSize() * (320.0f / 65.0f));
            }
            sampleSizeInBits = 0;
        }
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
        if (formatCode != WaveTool.WAVE_FORMAT_PCM) {
            long samples = 0;
            if (lLength != AudioSystem.NOT_SPECIFIED) {
                samples = lLength / format.getFrameSize() * decodedSamplesPerBlock;
            }
            if (samples > 0xFFFFFFFFl) {
                samples = (0xFFFFFFFFl / decodedSamplesPerBlock) * decodedSamplesPerBlock;
            }
            dos.writeInt(WaveTool.WAVE_FACT_MAGIC);
            dos.writeLittleEndian32(4);
            dos.writeLittleEndian32((int) (samples & 0xFFFFFFFF));
        }
        dos.writeInt(WaveTool.WAVE_DATA_MAGIC);
        dos.writeLittleEndian32((lLength != AudioSystem.NOT_SPECIFIED) ? ((int) lLength) : LENGTH_NOT_KNOWN);
    }
