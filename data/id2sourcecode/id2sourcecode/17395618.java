    public void writeReset() throws IOException {
        if (!(file instanceof RandomDataOutput)) throw new IOException("Cannot write to read only BitFile file");
        writeFile = (RandomDataOutput) file;
        outBuffer = new ByteArrayOutputStream();
        if (bitOffset != 0) {
            isLastByteIncomplete = true;
            try {
                byteOffset = file.length() - 1;
                file.seek(byteOffset);
                buffer = file.readByte();
            } catch (IOException ioe) {
                logger.error("Input/output exception while reading file. Stack trace follows.", ioe);
            }
        } else {
            isLastByteIncomplete = false;
            buffer = 0;
        }
    }
