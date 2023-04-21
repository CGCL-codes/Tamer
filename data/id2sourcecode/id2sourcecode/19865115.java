    private void writeQuad() {
        try {
            this.zos.putNextEntry(new ZipEntry("quad.bin"));
            onAdditionalQuadData(this.connect);
            new ObjectOutputStream(this.zos).writeObject(this.quad);
            this.zos.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
