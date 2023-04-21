    public boolean updateEntry(int i, File file) {
        if (file == null) {
            log.warning("No File");
            return false;
        }
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            log.warning("not added - " + file + ", Exists=" + file.exists() + ", Directory=" + file.isDirectory());
            return false;
        }
        log.fine("updateEntry - " + file);
        @SuppressWarnings("unused") String name = file.getName();
        byte[] data = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 8];
            int length = -1;
            while ((length = fis.read(buffer)) != -1) os.write(buffer, 0, length);
            fis.close();
            data = os.toByteArray();
            os.close();
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "(file)", ioe);
        }
        return updateEntry(i, data);
    }
