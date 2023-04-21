    public static ByteArrayInputStream bufferedRead(URL url) {
        try {
            URLStreamManager manager = new URLStreamManager(url);
            InputStream stream = manager.open();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read = 0;
            byte[] buffer = new byte[1204];
            while ((read = stream.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            manager.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }
