    private void writeConstData() {
        try {
            this.zos.putNextEntry(new ZipEntry("const.bin"));
            DataOutputStream outFile = new DataOutputStream(this.zos);
            this.buf.position(0);
            outFile.writeDouble(-1.);
            this.quad.writeConstData(this.buf);
            outFile.writeInt(this.buf.position());
            outFile.write(this.buf.array(), 0, this.buf.position());
            this.zos.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
