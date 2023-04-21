    protected File createLocalFile() throws IOException {
        File tempFile = File.createTempFile(FILE_PREFIX, null, new File(this.tempDir));
        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(tempFile), this.diskBufferSize);
        int read = 0;
        byte buffer[] = new byte[this.diskBufferSize];
        while ((read = this.inputStream.read(buffer, 0, this.diskBufferSize)) > 0) {
            fos.write(buffer, 0, read);
        }
        fos.flush();
        fos.close();
        return tempFile;
    }
