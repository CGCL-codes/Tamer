    public BufferedWriter getWriter() {
        if (writer == null) {
            if (file.exists()) {
                throw new IllegalStateException(String.format("Cannot create a writer because the file, %s, already exists", file.getAbsoluteFile()));
            } else if (this.memoryBuffer != null) {
                throw new IllegalStateException("Cannot create a writer because the memory buffer has already been written to.");
            }
            this.memoryBuffer = new StringBuilder();
            this.writer = new BufferedWriter(new ThresholdFileWriter(threshold, this.memoryBuffer, this.file));
        }
        return writer;
    }
