    public void serialize(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ZipOutputStream out = new ZipOutputStream(fos);
        out.setLevel(Deflater.DEFAULT_COMPRESSION);
        out.putNextEntry(new ZipEntry("imsmanifest.xml"));
        try {
            cp.getRootManifest().serialize(out);
        } catch (Unserializable x) {
            log.error("Taggy threw unserializable, should be impossible", x);
            throw new IOException();
        }
        out.closeEntry();
        ((PackageDirectoryImpl) cp.getRootDirectory()).serialize(out);
        out.close();
    }
