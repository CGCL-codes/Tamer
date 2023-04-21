    protected void createFile(JarFile file, ZipEntry entry, File target) {
        if (_buffer == null) {
            _buffer = new byte[COPY_BUFFER_SIZE];
        }
        File pdir = target.getParentFile();
        if (!pdir.exists()) {
            if (!pdir.mkdirs()) {
                log.warning("Failed to create parent for '" + target + "'.");
            }
        }
        InputStream in = null;
        FileOutputStream fout = null;
        try {
            in = file.getInputStream(entry);
            fout = new FileOutputStream(target);
            int total = 0, read;
            while ((read = in.read(_buffer)) != -1) {
                total += read;
                fout.write(_buffer, 0, read);
                updateProgress(total);
            }
        } catch (IOException ioe) {
            System.err.println("Error creating '" + target + "': " + ioe);
        } finally {
            StreamUtil.close(in);
            StreamUtil.close(fout);
        }
    }
