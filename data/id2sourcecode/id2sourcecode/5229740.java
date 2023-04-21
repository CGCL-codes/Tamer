    public void compress(ZipOutputStream output, String prefix) throws IOException {
        Iterator<IVirtualArtifact> artifacts = this.getArtifacts();
        while (artifacts.hasNext()) {
            IVirtualArtifact artifact = artifacts.next();
            String name = prefix + (artifact instanceof IVirtualFile ? artifact.getName() : artifact.getName() + "/");
            log.info("Adding " + name + " to compressed archive");
            try {
                ZipEntry entry = new ZipEntry(name);
                output.putNextEntry(entry);
                if (artifact instanceof IVirtualFile) {
                    IOUtil.transfer(((IVirtualFile) artifact).getInputStream(), output);
                }
                output.closeEntry();
            } catch (ZipException e) {
                if (e.getMessage().contains("duplicate entry")) {
                    log.warn("Ignoring duplicate artifact: " + name);
                } else throw e;
            }
            if (artifact instanceof IVirtualDirectory) {
                ((IVirtualDirectory) artifact).compress(output, prefix + artifact.getName() + "/");
            }
        }
    }
