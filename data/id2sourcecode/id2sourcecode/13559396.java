    private void writeQuad() throws IOException {
        this.zos.putNextEntry(new ZipEntry("quad.bin"));
        onAdditionalQuadData(this.connect);
        new ObjectOutputStream(this.zos).writeObject(this.quad);
        this.zos.closeEntry();
        this.zos.putNextEntry(new ZipEntry("connect.bin"));
        log.info("writing ConnectionManager to file...");
        this.connect.logEntries();
        new ObjectOutputStream(this.zos).writeObject(this.connect);
        this.zos.closeEntry();
    }
