    private void writeInfos() throws IOException {
        this.zos.putNextEntry(new ZipEntry("info.bin"));
        this.outFile = new DataOutputStream(this.zos);
        this.outFile.writeInt(OTFFileWriter.VERSION);
        this.outFile.writeInt(OTFFileWriter.MINORVERSION);
        this.outFile.writeDouble(this.interval_s);
        this.zos.closeEntry();
    }
