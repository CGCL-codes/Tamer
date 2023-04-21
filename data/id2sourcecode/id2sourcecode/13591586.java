    @Override
    public ProjectContext extract(final InputStream input) {
        Assertion.notNull(input);
        ZipInputStream is = null;
        try {
            is = new ZipInputStream(new BufferedInputStream(input));
            ZipEntry entry = null;
            while ((entry = is.getNextEntry()) != null) {
                final String name = entry.getName();
                if (entry.isDirectory()) {
                    new File(this.rootDir, name).mkdirs();
                } else {
                    new File(this.rootDir, new File(name).getParent()).mkdirs();
                    File file = new File(this.rootDir, name);
                    CheckedOutputStream out = new CheckedOutputStream(new BufferedOutputStream(new FileOutputStream(file)), new CRC32());
                    byte[] buf = new byte[BUF_SIZE];
                    int writeSize = 0;
                    int totalSize = 0;
                    while ((writeSize = is.read(buf)) != -1) {
                        totalSize += writeSize;
                        out.write(buf, 0, writeSize);
                    }
                    out.close();
                }
                is.closeEntry();
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            CloseableUtil.close(is);
            CloseableUtil.close(input);
        }
        return null;
    }
