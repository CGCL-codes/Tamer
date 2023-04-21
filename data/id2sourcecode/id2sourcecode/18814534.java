    protected void writeHeader() throws IOException {
        if (TDebug.TraceAudioOutputStream) {
            TDebug.out("AuAudioOutputStream.writeHeader(): called.");
        }
        AudioFormat format = getFormat();
        long lLength = getLength();
        TDataOutputStream dos = getDataOutputStream();
        if (TDebug.TraceAudioOutputStream) {
            TDebug.out("AuAudioOutputStream.writeHeader(): AudioFormat: " + format);
            TDebug.out("AuAudioOutputStream.writeHeader(): length: " + lLength);
        }
        dos.writeInt(AuTool.AU_HEADER_MAGIC);
        dos.writeInt(AuTool.DATA_OFFSET + getTextLength(description));
        dos.writeInt((lLength != AudioSystem.NOT_SPECIFIED) ? ((int) lLength) : AuTool.AUDIO_UNKNOWN_SIZE);
        dos.writeInt(AuTool.getFormatCode(format));
        dos.writeInt((int) format.getSampleRate());
        dos.writeInt(format.getChannels());
        writeText(dos, description);
    }
