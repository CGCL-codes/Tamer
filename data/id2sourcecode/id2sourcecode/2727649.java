        public void generateChunk(AudioFileFormat.Type fileFormat, AudioFormat format, long lLength, TDataOutputStream dos) throws IOException {
            int nCommChunkSize = ChunkTool.COMM_CHUNK_LENGTH;
            boolean aifc = fileFormat.equals(AudioFileFormat.Type.AIFC);
            if (aifc) {
                nCommChunkSize += 6;
            }
            int nFormatCode = AiffTool.getFormatCode(format);
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
            if (aifc) {
                dos.writeInt(nFormatCode);
                dos.writeShort(0);
            }
        }
