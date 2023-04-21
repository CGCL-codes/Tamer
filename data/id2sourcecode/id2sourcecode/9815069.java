    public int write(AudioInputStream stream, AudioFileFormat.Type fileType, File out) throws IOException {
        AiffFileFormat aiffFileFormat = (AiffFileFormat) getAudioFileFormat(fileType, stream);
        FileOutputStream fos = new FileOutputStream(out);
        BufferedOutputStream bos = new BufferedOutputStream(fos, bisBufferSize);
        int bytesWritten = writeAiffFile(stream, aiffFileFormat, bos);
        bos.close();
        if (aiffFileFormat.getByteLength() == AudioSystem.NOT_SPECIFIED) {
            int ssndBlockSize = (aiffFileFormat.getFormat().getChannels() * aiffFileFormat.getFormat().getSampleSizeInBits());
            int aiffLength = bytesWritten;
            int ssndChunkSize = aiffLength - aiffFileFormat.getHeaderSize() + 16;
            long dataSize = ssndChunkSize - 16;
            int numFrames = (int) (dataSize * 8 / ssndBlockSize);
            RandomAccessFile raf = new RandomAccessFile(out, "rw");
            raf.skipBytes(4);
            raf.writeInt(aiffLength - 8);
            raf.skipBytes(4 + aiffFileFormat.getFverChunkSize() + 4 + 4 + 2);
            raf.writeInt(numFrames);
            raf.skipBytes(2 + 10 + 4);
            raf.writeInt(ssndChunkSize - 8);
            raf.close();
        }
        return bytesWritten;
    }
