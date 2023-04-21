    public File getComponentFile(Project project) throws BuildException {
        if (generateName && zipFile == null) {
            try {
                zipFile = File.createTempFile(getComponentType(), ".jar");
                zipFile.deleteOnExit();
                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
                ZipEntry ze = new ZipEntry("META-INF/");
                ze.setSize(0);
                ze.setMethod(ZipEntry.STORED);
                ze.setCrc(new CRC32().getValue());
                zos.putNextEntry(ze);
                zos.close();
            } catch (IOException ioe) {
                throw new BuildException(ioe);
            }
        }
        return zipFile;
    }
