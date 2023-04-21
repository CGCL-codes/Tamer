    private void add(String path, InputStream source, JarOutputStream target) throws IOException {
        BufferedInputStream in = null;
        try {
            JarEntry entry = new JarEntry(path);
            entry.setTime(System.currentTimeMillis());
            target.putNextEntry(entry);
            in = new BufferedInputStream(source);
            byte[] buf = new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) != -1) {
                target.write(buf, 0, read);
            }
            target.closeEntry();
        } finally {
            if (in != null) in.close();
        }
    }
