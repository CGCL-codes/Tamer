        public AbstractAudioChunk parseChunk(DataInputStream dis, int chunkLength) throws UnsupportedAudioFileException, IOException {
            String debugAdd = "";
            int read = WaveTool.MIN_FMT_CHUNK_LENGTH;
            if (chunkLength < WaveTool.MIN_FMT_CHUNK_LENGTH) {
                throw new UnsupportedAudioFileException("corrupt WAVE file: format chunk is too small");
            }
            short formatCode = readLittleEndianShort(dis);
            short channelCount = readLittleEndianShort(dis);
            if (channelCount <= 0) {
                throw new UnsupportedAudioFileException("corrupt WAVE file: number of channels must be positive");
            }
            int sampleRate = readLittleEndianInt(dis);
            if (sampleRate <= 0) {
                throw new UnsupportedAudioFileException("corrupt WAVE file: sample rate must be positive");
            }
            int avgBytesPerSecond = readLittleEndianInt(dis);
            int blockAlign = readLittleEndianShort(dis);
            AudioFormat.Encoding encoding;
            int sampleSizeInBits;
            int frameSize = 0;
            float frameRate = (float) sampleRate;
            int cbSize = 0;
            switch(formatCode) {
                case WaveTool.WAVE_FORMAT_PCM:
                    if (chunkLength < WaveTool.MIN_FMT_CHUNK_LENGTH + 2) {
                        throw new UnsupportedAudioFileException("corrupt WAVE file: format chunk is too small");
                    }
                    sampleSizeInBits = readLittleEndianShort(dis);
                    if (sampleSizeInBits <= 0) {
                        throw new UnsupportedAudioFileException("corrupt WAVE file: sample size must be positive");
                    }
                    encoding = (sampleSizeInBits <= 8) ? AudioFormat.Encoding.PCM_UNSIGNED : AudioFormat.Encoding.PCM_SIGNED;
                    if (TDebug.TraceAudioFileReader) {
                        debugAdd += ", wBitsPerSample=" + sampleSizeInBits;
                    }
                    read += 2;
                    break;
                case WaveTool.WAVE_FORMAT_ALAW:
                    sampleSizeInBits = 8;
                    encoding = AudioFormat.Encoding.ALAW;
                    break;
                case WaveTool.WAVE_FORMAT_ULAW:
                    sampleSizeInBits = 8;
                    encoding = AudioFormat.Encoding.ULAW;
                    break;
                case WaveTool.WAVE_FORMAT_GSM610:
                    if (chunkLength < WaveTool.MIN_FMT_CHUNK_LENGTH + 6) {
                        throw new UnsupportedAudioFileException("corrupt WAVE file: extra GSM bytes are missing");
                    }
                    sampleSizeInBits = readLittleEndianShort(dis);
                    cbSize = readLittleEndianShort(dis);
                    if (cbSize < 2) {
                        throw new UnsupportedAudioFileException("corrupt WAVE file: extra GSM bytes are corrupt");
                    }
                    int decodedSamplesPerBlock = readLittleEndianShort(dis) & 0xFFFF;
                    if (TDebug.TraceAudioFileReader) {
                        debugAdd += ", wBitsPerSample=" + sampleSizeInBits + ", cbSize=" + cbSize + ", wSamplesPerBlock=" + decodedSamplesPerBlock;
                    }
                    sampleSizeInBits = AudioSystem.NOT_SPECIFIED;
                    encoding = WaveTool.GSM0610;
                    frameSize = blockAlign;
                    frameRate = ((float) sampleRate) / ((float) decodedSamplesPerBlock);
                    read += 6;
                    break;
                case WaveTool.WAVE_FORMAT_IMA_ADPCM:
                    if (chunkLength < WaveTool.MIN_FMT_CHUNK_LENGTH + 2) {
                        throw new UnsupportedAudioFileException("corrupt WAVE file: extra GSM bytes are missing");
                    }
                    sampleSizeInBits = readLittleEndianShort(dis);
                    cbSize = readLittleEndianShort(dis);
                    if (cbSize < 2) {
                        throw new UnsupportedAudioFileException("corrupt WAVE file: extra IMA ADPCM bytes are corrupt");
                    }
                    int samplesPerBlock = readLittleEndianShort(dis) & 0xFFFF;
                    if (TDebug.TraceAudioFileReader) {
                        debugAdd += ", wBitsPerSample=" + sampleSizeInBits + ", cbSize=" + cbSize + ", wSamplesPerBlock=" + samplesPerBlock;
                    }
                    sampleSizeInBits = AudioSystem.NOT_SPECIFIED;
                    encoding = WaveTool.GSM0610;
                    frameSize = blockAlign;
                    frameRate = ((float) sampleRate) / ((float) samplesPerBlock);
                    read += 6;
                    break;
                default:
                    throw new UnsupportedAudioFileException("unsupported WAVE file: unknown format code " + formatCode);
            }
            if (frameSize == 0) {
                frameSize = calculateFrameSize(sampleSizeInBits, channelCount);
            }
            if (TDebug.TraceAudioFileReader) {
                TDebug.out("WaveAudioFileReader.readFormatChunk():");
                TDebug.out("  read values: wFormatTag=" + formatCode + ", nChannels=" + channelCount + ", nSamplesPerSec=" + sampleRate + ", nAvgBytesPerSec=" + avgBytesPerSecond + ", nBlockAlign==" + blockAlign + debugAdd);
                TDebug.out("  constructed values: " + "encoding=" + encoding + ", sampleRate=" + ((float) sampleRate) + ", sampleSizeInBits=" + sampleSizeInBits + ", channels=" + channelCount + ", frameSize=" + frameSize + ", frameRate=" + frameRate);
            }
            final AudioFormat.Encoding f_encoding = encoding;
            final int f_sampleSizeInBits = sampleSizeInBits;
            final int f_channelCount = channelCount;
            final int f_frameSize = frameSize;
            final float f_frameRate = frameRate;
            ChunkTool.advanceWaveChunk(dis, chunkLength, read);
            return new WaveFmtChunk() {

                public AudioFormat.Encoding getEncoding() {
                    return f_encoding;
                }

                public int getSampleSizeInBits() {
                    return f_sampleSizeInBits;
                }

                public int getChannelCount() {
                    return f_channelCount;
                }

                public int getFrameSize() {
                    return f_frameSize;
                }

                public float getFrameRate() {
                    return f_frameRate;
                }

                public String getAudioFormatPropertyKey() {
                    return null;
                }
            };
        }
