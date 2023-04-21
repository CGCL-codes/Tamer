    protected void writeHeader() throws IOException {
        if (TDebug.TraceAudioOutputStream) {
            TDebug.out("AiffAudioOutputStream.writeHeader(): called.");
        }
        AudioFormat format = getFormat();
        boolean bIsAifc = m_FileType.equals(AudioFileFormat.Type.AIFC);
        long lLength = getLength();
        TDataOutputStream dos = getDataOutputStream();
        int nCommChunkSize = 18;
        int nFormatCode = AiffTool.getFormatCode(format);
        if (bIsAifc) {
            nCommChunkSize += 6;
        }
        int nHeaderSize = 4 + 8 + nCommChunkSize + 8;
        if (bIsAifc) {
            nHeaderSize += 12;
        }
        if (lLength != AudioSystem.NOT_SPECIFIED && lLength + nHeaderSize > 0x7FFFFFFFl) {
            lLength = 0x7FFFFFFFl - nHeaderSize;
        }
        long lSSndChunkSize = (lLength != AudioSystem.NOT_SPECIFIED) ? (lLength + (lLength % 2) + 8) : AudioSystem.NOT_SPECIFIED;
        dos.writeInt(AiffTool.AIFF_FORM_MAGIC);
        dos.writeInt((lLength != AudioSystem.NOT_SPECIFIED) ? ((int) (lSSndChunkSize + nHeaderSize)) : LENGTH_NOT_KNOWN);
        if (bIsAifc) {
            dos.writeInt(AiffTool.AIFF_AIFC_MAGIC);
            dos.writeInt(AiffTool.AIFF_FVER_MAGIC);
            dos.writeInt(4);
            dos.writeInt(AiffTool.AIFF_FVER_TIME_STAMP);
        } else {
            dos.writeInt(AiffTool.AIFF_AIFF_MAGIC);
        }
        dos.writeInt(AiffTool.AIFF_COMM_MAGIC);
        dos.writeInt(nCommChunkSize);
        dos.writeShort((short) format.getChannels());
        dos.writeInt((lLength != AudioSystem.NOT_SPECIFIED) ? ((int) (lLength / format.getFrameSize())) : LENGTH_NOT_KNOWN);
        if (nFormatCode == AiffTool.AIFF_COMM_ULAW) {
            dos.writeShort(16);
        } else {
            dos.writeShort((short) format.getSampleSizeInBits());
        }
        writeIeeeExtended(dos, format.getSampleRate());
        if (bIsAifc) {
            dos.writeInt(nFormatCode);
            dos.writeShort(0);
        }
        dos.writeInt(AiffTool.AIFF_SSND_MAGIC);
        dos.writeInt((lLength != AudioSystem.NOT_SPECIFIED) ? ((int) (lLength + 8)) : LENGTH_NOT_KNOWN);
        dos.writeInt(0);
        dos.writeInt(0);
    }
