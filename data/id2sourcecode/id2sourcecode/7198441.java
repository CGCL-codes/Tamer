    private byte[] loadFile(File file) {
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
            log.log(Level.SEVERE, "addEntry (file)", ioe);
        }
        return data;
    }
