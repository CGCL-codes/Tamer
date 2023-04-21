    public boolean addEntry(File file) {
        if (file == null) {
            log.warning("No File");
            return false;
        }
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            log.warning("not added - " + file + ", Exists=" + file.exists() + ", Directory=" + file.isDirectory());
            return false;
        }
        log.fine("addEntry - " + file);
        String name = file.getName();
        byte[] data = null;
        FileInputStream fis = null;
        ByteArrayOutputStream os = null;
        try {
            fis = new FileInputStream(file);
            os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 8];
            int length = -1;
            while ((length = fis.read(buffer)) != -1) os.write(buffer, 0, length);
            data = os.toByteArray();
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "(file)", ioe);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    log.log(Level.SEVERE, "(file)", ex);
                }
                ;
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    log.log(Level.SEVERE, "(file)", ex);
                }
                ;
            }
        }
        return addEntry(name, data);
    }
