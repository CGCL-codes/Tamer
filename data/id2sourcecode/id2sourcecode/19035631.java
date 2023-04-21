    private void copyJarEntries(String sourceJarFile, JarOutputStream jos) throws IOException {
        FileInputStream fis = new FileInputStream(sourceJarFile);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry zeSrc, zeDst;
        while ((zeSrc = zis.getNextEntry()) != null) {
            zeDst = new ZipEntry(zeSrc.getName());
            jos.putNextEntry(zeDst);
            copyIStoOS(zis, jos, 4096);
            zis.closeEntry();
            jos.closeEntry();
        }
        zis.close();
        fis.close();
    }
