    public void writeReset() throws IOException {
        if (!(file instanceof RandomDataOutput)) throw new IOException("Cannot write to read only BitFile file");
        writeFile = (RandomDataOutput) file;
        this.isWriteMode = true;
    }
