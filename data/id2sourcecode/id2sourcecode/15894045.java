    public void close() {
        try {
            for (ManifestModifier mm : this.modifiers) {
                mm.modify(this.manifest);
            }
            ZipEntry e = new ZipEntry(JarFile.MANIFEST_NAME);
            this.stream.putNextEntry(e);
            this.manifest.write(this.stream);
            this.stream.closeEntry();
            this.stream.close();
            this.modifiers.clear();
            this.existsDirectories.clear();
        } catch (Exception e) {
            handle(e);
        }
    }
