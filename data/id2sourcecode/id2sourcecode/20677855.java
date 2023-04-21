    public void addArchive(ZipInputStream in) throws ZipException, IOException {
        ZipEntry ze = null;
        int read;
        byte buffer[] = new byte[8192];
        do {
            ze = in.getNextEntry();
            if (ze != null) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                while ((read = in.read(buffer)) >= 0) {
                    os.write(buffer, 0, read);
                }
                zipLazy.put(ze.getName(), os.toByteArray());
            }
        } while (ze != null);
    }
