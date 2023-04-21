    public void prepare() throws IOException {
        this.extractDirectory = TempFileFactory.get().createTempDirectory("pdash-compressed-wd", ".tmp");
        File srcZip = getTargetZipFile();
        InputStream in = new FileInputStream(srcZip);
        if (isPdbk(srcZip)) in = new XorInputStream(in, PDBK_XOR_BITS);
        boolean sawEntry = false;
        try {
            ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(in));
            ZipEntry e;
            while ((e = zipIn.getNextEntry()) != null) {
                sawEntry = true;
                if (!e.isDirectory()) {
                    String filename = e.getName();
                    File f = new File(extractDirectory, filename);
                    if (filename.indexOf('/') != -1) f.getParentFile().mkdirs();
                    FileUtils.copyFile(zipIn, f);
                    f.setLastModified(e.getTime());
                }
            }
        } finally {
            FileUtils.safelyClose(in);
        }
        if (!sawEntry) throw new ZipException("Not a valid ZIP file: " + srcZip);
    }
