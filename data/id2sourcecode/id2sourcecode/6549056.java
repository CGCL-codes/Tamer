    void extractFile(ZipInputStream zis, ZipEntry e) throws IOException {
        String name = e.getName();
        File f = new File(uploadDirectory() + File.separatorChar + e.getName().replace('/', File.separatorChar));
        if (e.isDirectory()) {
            if (!f.exists() && !f.mkdirs() || !f.isDirectory()) throw new IOException("Can't create " + f.getPath());
            if (log.isDebugEnabled()) log.debug("Processing: " + name);
        } else {
            if (f.getParent() != null) {
                File d = new File(f.getParent());
                if (!d.exists() && !d.mkdirs() || !d.isDirectory()) throw new IOException("Can't create " + d.getPath());
            }
            OutputStream os = new FileOutputStream(f);
            byte[] b = new byte[512];
            int len;
            while ((len = zis.read(b, 0, b.length)) != -1) os.write(b, 0, len);
            zis.closeEntry();
            os.close();
            if (log.isDebugEnabled()) {
                if (e.getMethod() == 8) log.debug("Extracted: " + name); else log.debug("Inflated: " + name);
            }
        }
    }
