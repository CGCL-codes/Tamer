    private byte[] getSound(String fileName) {
        try {
            URL url = Talker.class.getResource(fileName);
            AudioInputStream stream = AudioSystem.getAudioInputStream(url);
            AudioFormat format = stream.getFormat();
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat tmpFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                stream = AudioSystem.getAudioInputStream(tmpFormat, stream);
                format = tmpFormat;
            }
            DataLine.Info info = new DataLine.Info(Clip.class, format, ((int) stream.getFrameLength() * format.getFrameSize()));
            if (line == null) {
                DataLine.Info outInfo = new DataLine.Info(SourceDataLine.class, format);
                if (!AudioSystem.isLineSupported(outInfo)) {
                    System.out.println("Line matching " + outInfo + " not supported.");
                    throw new Exception("Line matching " + outInfo + " not supported.");
                }
                line = (SourceDataLine) AudioSystem.getLine(outInfo);
                line.open(format, 50000);
                line.start();
            }
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead = 0;
            if ((numBytesRead = stream.read(data)) != -1) {
                int numBytesRemaining = numBytesRead;
            }
            byte[] newData = new byte[numBytesRead];
            for (int i = 0; i < numBytesRead; i++) newData[i] = data[i];
            return newData;
        } catch (Exception e) {
            return new byte[0];
        }
    }
