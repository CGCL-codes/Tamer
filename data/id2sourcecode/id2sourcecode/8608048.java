    protected int writeImpl(AudioInputStream audioInputStream, AudioOutputStream audioOutputStream, boolean bNeedsConversion) throws IOException {
        if (TDebug.TraceAudioFileWriter) {
            TDebug.out(">TAudioFileWriter.writeImpl(): called");
            TDebug.out("class: " + getClass().getName());
        }
        int nTotalWritten = 0;
        AudioFormat inputFormat = audioInputStream.getFormat();
        AudioFormat outputFormat = audioOutputStream.getFormat();
        int nBytesPerSample = outputFormat.getFrameSize() / outputFormat.getChannels();
        int nBufferSize = ((int) BUFFER_LENGTH / outputFormat.getFrameSize()) * outputFormat.getFrameSize();
        byte[] abBuffer = new byte[nBufferSize];
        while (true) {
            if (TDebug.TraceAudioFileWriter) {
                TDebug.out("trying to read (bytes): " + abBuffer.length);
            }
            int nBytesRead = audioInputStream.read(abBuffer);
            if (TDebug.TraceAudioFileWriter) {
                TDebug.out("read (bytes): " + nBytesRead);
            }
            if (nBytesRead == -1) {
                break;
            }
            if (bNeedsConversion) {
                TConversionTool.changeOrderOrSign(abBuffer, 0, nBytesRead, nBytesPerSample);
            }
            int nWritten = audioOutputStream.write(abBuffer, 0, nBytesRead);
            nTotalWritten += nWritten;
        }
        if (TDebug.TraceAudioFileWriter) {
            TDebug.out("<TAudioFileWriter.writeImpl(): after main loop. Wrote " + nTotalWritten + " bytes");
        }
        audioOutputStream.close();
        return nTotalWritten;
    }
