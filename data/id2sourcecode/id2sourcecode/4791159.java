    private InputStream getFileStream(WaveFileFormat waveFileFormat, InputStream audioStream) throws IOException {
        AudioFormat audioFormat = waveFileFormat.getFormat();
        int headerLength = waveFileFormat.getHeaderSize();
        int riffMagic = WaveFileFormat.RIFF_MAGIC;
        int waveMagic = WaveFileFormat.WAVE_MAGIC;
        int fmtMagic = WaveFileFormat.FMT_MAGIC;
        int fmtLength = WaveFileFormat.getFmtChunkSize(waveFileFormat.getWaveType());
        short wav_type = (short) waveFileFormat.getWaveType();
        short channels = (short) audioFormat.getChannels();
        short sampleSizeInBits = (short) audioFormat.getSampleSizeInBits();
        int sampleRate = (int) audioFormat.getSampleRate();
        int frameSizeInBytes = (int) audioFormat.getFrameSize();
        int frameRate = (int) audioFormat.getFrameRate();
        int avgBytesPerSec = channels * sampleSizeInBits * sampleRate / 8;
        ;
        short blockAlign = (short) ((sampleSizeInBits / 8) * channels);
        int dataMagic = WaveFileFormat.DATA_MAGIC;
        int dataLength = waveFileFormat.getFrameLength() * frameSizeInBytes;
        int length = waveFileFormat.getByteLength();
        int riffLength = dataLength + headerLength - 8;
        byte header[] = null;
        ByteArrayInputStream headerStream = null;
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        SequenceInputStream waveStream = null;
        AudioFormat audioStreamFormat = null;
        AudioFormat.Encoding encoding = null;
        InputStream codedAudioStream = audioStream;
        if (audioStream instanceof AudioInputStream) {
            audioStreamFormat = ((AudioInputStream) audioStream).getFormat();
            encoding = audioStreamFormat.getEncoding();
            if (AudioFormat.Encoding.PCM_SIGNED.equals(encoding)) {
                if (sampleSizeInBits == 8) {
                    wav_type = WaveFileFormat.WAVE_FORMAT_PCM;
                    codedAudioStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, audioStreamFormat.getSampleRate(), audioStreamFormat.getSampleSizeInBits(), audioStreamFormat.getChannels(), audioStreamFormat.getFrameSize(), audioStreamFormat.getFrameRate(), false), (AudioInputStream) audioStream);
                }
            }
            if ((AudioFormat.Encoding.PCM_SIGNED.equals(encoding) && audioStreamFormat.isBigEndian()) || (AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding) && !audioStreamFormat.isBigEndian()) || (AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding) && audioStreamFormat.isBigEndian())) {
                if (sampleSizeInBits != 8) {
                    wav_type = WaveFileFormat.WAVE_FORMAT_PCM;
                    codedAudioStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioStreamFormat.getSampleRate(), audioStreamFormat.getSampleSizeInBits(), audioStreamFormat.getChannels(), audioStreamFormat.getFrameSize(), audioStreamFormat.getFrameRate(), false), (AudioInputStream) audioStream);
                }
            }
        }
        baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
        dos.writeInt(riffMagic);
        dos.writeInt(big2little(riffLength));
        dos.writeInt(waveMagic);
        dos.writeInt(fmtMagic);
        dos.writeInt(big2little(fmtLength));
        dos.writeShort(big2littleShort(wav_type));
        dos.writeShort(big2littleShort(channels));
        dos.writeInt(big2little(sampleRate));
        dos.writeInt(big2little(avgBytesPerSec));
        dos.writeShort(big2littleShort(blockAlign));
        dos.writeShort(big2littleShort(sampleSizeInBits));
        if (wav_type != WaveFileFormat.WAVE_FORMAT_PCM) {
            dos.writeShort(0);
        }
        dos.writeInt(dataMagic);
        dos.writeInt(big2little(dataLength));
        dos.close();
        header = baos.toByteArray();
        headerStream = new ByteArrayInputStream(header);
        waveStream = new SequenceInputStream(headerStream, codedAudioStream);
        return (InputStream) waveStream;
    }
