    public static FilePtr get(String fnamp) throws IOException {
        FilePtrJar fp = null;
        boolean isJar = fnamp.startsWith("jar") || fnamp.startsWith("zip");
        try {
            fnamp = fnamp.replace('\\', '/');
            URL url = new URL(fnamp);
            if (!url.getProtocol().equals("jar")) {
                return null;
            }
            String f = url.getFile();
            String[] fa = f.split("!");
            url = new URL(fa[0]);
            String entryName = fa[1].substring(1);
            if (url.getProtocol().equals("file")) {
                f = url.getFile();
                FileInputStream in = new FileInputStream(f);
                ZipInputStream zin = new ZipInputStream(in);
                ZipEntry ze;
                while ((ze = zin.getNextEntry()) != null) {
                    if (ze.getName().equals(entryName) && !ze.isDirectory()) {
                        break;
                    }
                }
                if (ze == null) {
                    LOG.warning("Not found: " + fnamp + "???");
                } else {
                    if (ze.getMethod() != ZipEntry.STORED) {
                        LOG.warning("Found " + fnamp + " but it is COMPRESSED");
                    } else {
                        long size = ze.getCompressedSize();
                        long entryOffset = in.getChannel().position();
                        RandomAccessFile raf = new RandomAccessFile(f, "r");
                        zin.close();
                        raf.seek(entryOffset);
                        fp = new FilePtrJar(raf, fnamp, entryOffset, size);
                    }
                }
            }
        } catch (MalformedURLException ex) {
            if (isJar) LOG.warning("FilePtrJar: " + ex.getMessage());
        } catch (FileNotFoundException ex) {
            if (isJar) LOG.info(ex.getMessage());
        }
        return fp;
    }
