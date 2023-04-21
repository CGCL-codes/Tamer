    public void setKeystoreFile(String path) {
        FileInputStream fis = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                fis = new FileInputStream(file);
                FileChannel fc = fis.getChannel();
                ByteBuffer fb = ByteBuffer.allocate(Long.valueOf(file.length()).intValue());
                fc.read(fb);
                fb.flip();
                keystore = IoBuffer.wrap(fb).array();
            } else {
                log.warn("Keystore file does not exist: {}", path);
            }
            file = null;
        } catch (Exception e) {
            log.warn("Error setting keystore data", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
    }
